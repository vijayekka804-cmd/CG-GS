package com.example.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import android.util.LruCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit

class PdfDocumentManager(private val context: Context) {

    companion object {
        private const val TAG = "PdfDocumentManager"
        private const val TARGET_RENDER_WIDTH = 1200 // Crisp high-definition normalized width
    }

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .followRedirects(true)
        .followSslRedirects(true)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    // Cache rendered pages in memory for super smooth scrolling
    private val memoryCache: LruCache<Int, Bitmap> = object : LruCache<Int, Bitmap>(
        (Runtime.getRuntime().maxMemory() / 1024 / 4).toInt() // Use 1/4th of available memory for cache
    ) {
        override fun sizeOf(key: Int, value: Bitmap): Int {
            return value.byteCount / 1024
        }
    }

    private var parcelFileDescriptor: ParcelFileDescriptor? = null
    private var pdfRenderer: PdfRenderer? = null
    var pageCount: Int = 0
        private set
    var isInitialized: Boolean = false
        private set

    /**
     * Downloads (or loads from cache) the PDF and initializes the renderer.
     * @param url Google Drive download URL
     * @param cacheKey Unique identifier for the chapter
     * @param onProgress Callback with progress percentage (0-100) or -1 for indeterminate
     */
    suspend fun loadPdf(
        url: String,
        cacheKey: String,
        forceRefresh: Boolean = false,
        onProgress: (Int) -> Unit = {}
    ): Result<Int> = withContext(Dispatchers.IO) {
        try {
            close() // close any previous instances

            val cacheDir = File(context.cacheDir, "pdfs").apply { mkdirs() }
            val localFile = File(cacheDir, "chapter_$cacheKey.pdf")

            // Check if valid cached file exists
            val needsDownload = forceRefresh || !localFile.exists() || localFile.length() < 1024

            if (needsDownload) {
                onProgress(5)
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", "Mozilla/5.0 (Android; Mobile)")
                    .build()

                val response = httpClient.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext Result.failure(
                        Exception("PDF डाउनलोड विफल (HTTP ${response.code})")
                    )
                }

                val responseBody = response.body ?: return@withContext Result.failure(
                    Exception("खाली उत्तर प्राप्त हुआ")
                )

                val contentLength = responseBody.contentLength()
                val tempFile = File(cacheDir, "temp_chapter_$cacheKey.pdf")

                var inputStream: InputStream? = null
                var outputStream: FileOutputStream? = null
                try {
                    inputStream = responseBody.byteStream()
                    outputStream = FileOutputStream(tempFile)
                    val buffer = ByteArray(8 * 1024)
                    var bytesRead: Int
                    var totalRead = 0L

                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                        totalRead += bytesRead
                        if (contentLength > 0) {
                            val progress = (totalRead * 100 / contentLength).toInt().coerceIn(5, 95)
                            onProgress(progress)
                        }
                    }
                    outputStream.flush()
                } finally {
                    inputStream?.close()
                    outputStream?.close()
                }

                // Verify file is a valid PDF (starts with %PDF)
                if (!isValidPdfHeader(tempFile)) {
                    tempFile.delete()
                    return@withContext Result.failure(
                        Exception("अमान्य PDF प्रारूप")
                    )
                }

                if (localFile.exists()) {
                    localFile.delete()
                }
                tempFile.renameTo(localFile)
            }

            onProgress(98)

            // Open renderer
            val pfd = ParcelFileDescriptor.open(localFile, ParcelFileDescriptor.MODE_READ_ONLY)
            parcelFileDescriptor = pfd
            val renderer = PdfRenderer(pfd)
            pdfRenderer = renderer
            pageCount = renderer.pageCount
            isInitialized = true
            memoryCache.evictAll()

            onProgress(100)
            Result.success(pageCount)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading PDF: ${e.message}", e)
            close()
            Result.failure(e)
        }
    }

    private fun isValidPdfHeader(file: File): Boolean {
        if (!file.exists() || file.length() < 10) return false
        return try {
            file.inputStream().use { stream ->
                val header = ByteArray(5)
                stream.read(header)
                String(header).startsWith("%PDF")
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Renders a specific page to a Bitmap with consistent fit-to-width normalization.
     * Every page is scaled to a standard target width, maintaining its original aspect ratio,
     * so that page heights and widths remain visually consistent without size jumping.
     */
    suspend fun renderPage(pageIndex: Int): Bitmap? = withContext(Dispatchers.Default) {
        if (!isInitialized || pdfRenderer == null) return@withContext null
        if (pageIndex < 0 || pageIndex >= pageCount) return@withContext null

        // Check memory cache first
        memoryCache.get(pageIndex)?.let { return@withContext it }

        synchronized(this) {
            // Re-check after acquiring lock
            memoryCache.get(pageIndex)?.let { return@synchronized it }

            try {
                val renderer = pdfRenderer ?: return@synchronized null
                val page = renderer.openPage(pageIndex)

                val pageWidth = page.width
                val pageHeight = page.height

                // Calculate normalized target dimensions
                // Every page has the EXACT SAME rendered width: TARGET_RENDER_WIDTH
                val targetWidth = TARGET_RENDER_WIDTH
                val aspectRatio = pageHeight.toFloat() / pageWidth.toFloat()
                val targetHeight = (targetWidth * aspectRatio).toInt().coerceAtLeast(100)

                val bitmap = Bitmap.createBitmap(
                    targetWidth,
                    targetHeight,
                    Bitmap.Config.ARGB_8888
                )

                // Fill with white background
                val canvas = Canvas(bitmap)
                canvas.drawColor(Color.WHITE)

                // Render page content
                page.render(
                    bitmap,
                    null,
                    null,
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                )
                page.close()

                memoryCache.put(pageIndex, bitmap)
                bitmap
            } catch (e: Exception) {
                Log.e(TAG, "Error rendering page $pageIndex: ${e.message}", e)
                null
            }
        }
    }

    fun getLocalFile(cacheKey: String): File? {
        val file = File(File(context.cacheDir, "pdfs"), "chapter_$cacheKey.pdf")
        return if (file.exists() && file.length() > 0) file else null
    }

    fun close() {
        try {
            memoryCache.evictAll()
            pdfRenderer?.close()
            pdfRenderer = null
            parcelFileDescriptor?.close()
            parcelFileDescriptor = null
            pageCount = 0
            isInitialized = false
        } catch (e: Exception) {
            Log.e(TAG, "Error closing PdfDocumentManager: ${e.message}")
        }
    }
}
