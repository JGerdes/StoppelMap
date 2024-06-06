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
import java.util.UUID

class ImageProcessor(
    private val httpClient: HttpClient,
    private val imageLoader: ImmutableImageLoader,
    private val webpWriter: WebpWriter,
    private val imageCacheDirectory: File,
    private val logger: Logger,
) {

    private val originalsDirectory by lazy {
        File(imageCacheDirectory, "originals").also { it.mkdirs() }
    }
    private val processedDirectory by lazy {
        File(imageCacheDirectory, "processed").also { it.mkdirs() }
    }

    suspend operator fun invoke(
        url: String,
        articleSlug: String,
    ): Result {
        return try {
            val uuid = UUID.nameUUIDFromBytes(url.toByteArray()).toString()

            logger.debug("🏞️ Download $uuid ($url)")
            val originalFile = downloadImage(url, directory = File(
                originalsDirectory,
                articleSlug
            ).also { it.mkdir() })

            logger.debug("🏞️ Scale $uuid ($url)")
            val scaledImage = scaleImage(originalFile, File(processedDirectory, "$uuid.webp"))

            logger.debug("🏞️ BlurHash $uuid ($url)")
            val blurHash = generateBlurHash(scaledImage)

            logger.debug("🏞️ Done processing $uuid ($url)")

            Result.Success(
                uuid = uuid,
                blurHash = blurHash,
            )
        } catch (t: Throwable) {
            if (t is CancellationException) throw t
            Result.Error(t)
        }
    }

    private suspend fun downloadImage(imageUrl: String, directory: File): File {
        val name = imageUrl.split("/").last()
        val file = File(directory, name)
        if (!file.exists()) {
            val httpResponse: HttpResponse = httpClient.get(imageUrl) {
                onDownload { bytesSentTotal, contentLength ->
                    logger.trace("Received $bytesSentTotal bytes from $contentLength")
                }
            }
            if (httpResponse.status != HttpStatusCode.OK) throw IllegalStateException("Status code was not OK (was ${httpResponse.status}).")
            val responseBody: ByteArray = httpResponse.body()
            file.writeBytes(responseBody)
        }
        return file
    }

    private fun scaleImage(originalImage: File, destination: File): ImmutableImage =
        if (destination.exists()) {
            imageLoader.fromFile(originalImage)
        } else {
            val original = imageLoader.fromFile(originalImage)
            val targetSize = 1024
            val modified = original.bound(targetSize, targetSize)
            modified.output(webpWriter, destination)
            modified
        }

    private fun generateBlurHash(image: ImmutableImage): String {
        val newBufferedImage = image.toNewBufferedImage(BufferedImage.TYPE_INT_RGB)
        return BlurHash.encode(newBufferedImage, 4, 3)
    }

    sealed interface Result {
        data class Error(val throwable: Throwable) : Result
        data class Success(
            val uuid: String,
            val blurHash: String,
        ) : Result
    }
}