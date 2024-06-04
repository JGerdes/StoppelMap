package com.jonasgerdes.stoppelmap.server.crawler

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.ImmutableImageLoader
import com.sksamuel.scrimage.webp.WebpWriter
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.trbl.blurhash.BlurHash
import kotlinx.coroutines.CancellationException
import org.slf4j.Logger
import java.awt.image.BufferedImage
import java.io.File

class ImageProcessor(
    private val httpClient: HttpClient,
    private val imageLoader: ImmutableImageLoader,
    private val webpWriter: WebpWriter,
    private val imageCacheDirectory: File,
    private val logger: Logger,
) {
    suspend operator fun invoke(
        baseUrl: String,
        imageUrl: String,
        articleSlug: String,
    ): Result {
        return try {
            val url = sanitizeUrl(url = imageUrl, baseUrl = baseUrl)
            logger.debug("🏞️ Download $url")
            val originalFile = downloadImage(url, articleSlug)
            logger.debug("🏞️ Scale $url")
            val (scaledFile, scaledImage) = scaleImage(originalFile)
            logger.debug("🏞️ BlurHash $url")
            val blurHash = generateBlurHash(scaledImage)

            Result.Success(
                localFile = scaledFile,
                blurHash = blurHash,
            )
        } catch (t: Throwable) {
            if (t is CancellationException) throw t
            Result.Error(t)
        }
    }

    private suspend fun downloadImage(imageUrl: String, articleSlug: String): File {
        val name = imageUrl.split("/").last()
        val folder = File(imageCacheDirectory, articleSlug).also { it.mkdirs() }
        val file = File(folder, name)
        if (!file.exists()) {
            val httpResponse: HttpResponse = httpClient.get(imageUrl) {
                onDownload { bytesSentTotal, contentLength ->
                    logger.debug("Received $bytesSentTotal bytes from $contentLength")
                }
            }
            if (httpResponse.status != HttpStatusCode.OK) throw IllegalStateException("Status code was not OK (was ${httpResponse.status}).")
            val responseBody: ByteArray = httpResponse.body()
            file.writeBytes(responseBody)
        }
        return file
    }

    private fun scaleImage(originalImage: File): Pair<File, ImmutableImage> {
        val scaledFile = File(
            originalImage.parent,
            "scaled_${originalImage.nameWithoutExtension.lowercase()}.webp"
        )

        val original = imageLoader.fromFile(originalImage)
        val targetSize = 1024
        val modified = original.bound(targetSize, targetSize)
        modified.output(webpWriter, scaledFile)
        return scaledFile to modified
    }

    private fun generateBlurHash(image: ImmutableImage): String {
        val newBufferedImage = image.toNewBufferedImage(BufferedImage.TYPE_INT_RGB)
        return BlurHash.encode(newBufferedImage, 4, 3)
    }

    private fun sanitizeUrl(url: String, baseUrl: String) =
        when {
            url.startsWith("http") -> url
            else -> baseUrl + url.removePrefix("/")
        }

    sealed interface Result {
        data class Error(val throwable: Throwable) : Result
        data class Success(
            val localFile: File,
            val blurHash: String,
        ) : Result

    }
}