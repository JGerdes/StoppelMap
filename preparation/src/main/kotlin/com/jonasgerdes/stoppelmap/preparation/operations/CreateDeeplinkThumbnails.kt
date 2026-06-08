package com.jonasgerdes.stoppelmap.preparation.operations

import com.jonasgerdes.stoppelmap.preparation.Settings
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.Position
import com.sksamuel.scrimage.nio.ImageWriter
import com.sksamuel.scrimage.nio.ImmutableImageLoader
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class CreateDeeplinkThumbnails(private val imageWriter: ImageWriter) : KoinComponent {

    private val supportedExtensions = setOf("jpg", "jpeg")

    private val settings: Settings by inject()
    private val imageLoader: ImmutableImageLoader by inject()

    operator fun invoke(): List<Pair<String, String>> {
        val imageSettings = settings.imageSettings ?: return emptyList<Pair<String, String>>().also {
            System.err.println("WARN: No imageSettings found, skipping processing")
        }
        val overlayFile = imageSettings.deeplinkThumbOverlayImage!!

        return imageSettings.originalImageDir
            .listFiles { it.isDirectory }
            .also { print("generating ${it.size} thumbnails ") }
            .map { directory ->
                val imageFile =
                    directory.listFiles { it.isFile && it.extension.lowercase() in supportedExtensions }.first()
                val processedName = "thumb_${imageFile.nameWithoutExtension}.jpg"
                val procssedImageFile = File(imageSettings.processedImageDir, processedName)
                scaleToCoverAndAddOverlay(
                    originalImage = imageFile,
                    overlayImage = overlayFile,
                    destination = procssedImageFile,
                )
                print(".")
                directory.name to "${imageSettings.hostedImageBaseUrl}/$processedName"
            }.also {
                println(" done")
            }
    }


    private fun scaleToCoverAndAddOverlay(
        originalImage: File,
        overlayImage: File,
        destination: File,
    ): ImmutableImage =
        if (destination.exists()) {
            print("${destination.absolutePath} already exist, skipping")
            imageLoader.fromFile(destination)
        } else {
            try {
                val original = imageLoader.fromFile(originalImage)
                val overlay = imageLoader.fromFile(overlayImage)
                val modified = original
                    .cover(512, 512)
                    .overlay(overlay, Position.BottomLeft)
                modified.output(imageWriter, destination)
                modified
            } catch (e: Throwable) {
                System.err.println(e.message)
                e.printStackTrace()
                throw e
            }
        }
}