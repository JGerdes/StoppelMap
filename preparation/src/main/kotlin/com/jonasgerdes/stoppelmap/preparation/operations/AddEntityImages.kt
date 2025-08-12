package com.jonasgerdes.stoppelmap.preparation.operations

import com.jonasgerdes.stoppelmap.dto.data.Image
import com.jonasgerdes.stoppelmap.dto.data.MapEntity
import com.jonasgerdes.stoppelmap.preparation.Settings
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.ImageWriter
import com.sksamuel.scrimage.nio.ImmutableImageLoader
import io.trbl.blurhash.BlurHash
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.awt.image.BufferedImage
import java.io.File

class AddEntityImages : KoinComponent {

    private val supportedExtensions = setOf("jpg", "jpeg")

    private val settings: Settings by inject()
    private val imageLoader: ImmutableImageLoader by inject()
    private val imageWriter: ImageWriter by inject()

    operator fun invoke(input: List<MapEntity>): List<MapEntity> {
        val imageSettings = settings.imageSettings ?: return input.also {
            System.err.println("WARN: No imageSettings found, skipping processing")
        }

        val updatedEntities = mutableListOf<MapEntity>()
        imageSettings.originalImageDir
            .listFiles { it.isDirectory }
            .forEach { directory ->
                val entity = input.firstOrNull { it.slug == directory.name }
                if (entity == null) {
                    System.err.println("WARN: No entity found with slug ${directory.name}")
                    return@forEach
                }
                val newImages = mutableListOf<Image>()
                directory.listFiles { it.isFile && it.extension.lowercase() in supportedExtensions }
                    .forEachIndexed { index, imageFile ->
                        val processedName = "${entity.slug}-${index.toString().padStart(2, '0')}.webp"
                        val procssedImageFile = File(imageSettings.processedImageDir, processedName)
                        val result = scaleImage(imageFile, procssedImageFile)
                        val blurHash = generateBlurHash(result)
                        newImages += Image(
                            url = "${imageSettings.hostedImageBaseUrl}/$processedName",
                            caption = null,
                            copyright = null,
                            blurHash = blurHash,
                            preferredTheme = null,
                        )
                    }
                updatedEntities += entity.copy(
                    images = entity.images + newImages
                )
            }
        return input.map { og ->
            updatedEntities.firstOrNull { it.slug == og.slug } ?: og
        }
    }

    private fun scaleImage(originalImage: File, destination: File): ImmutableImage =
        if (destination.exists()) {
            imageLoader.fromFile(destination)
        } else {
            val original = imageLoader.fromFile(originalImage)
            val targetSize = 1024
            val modified = original.bound(targetSize, targetSize)
            modified.output(imageWriter, destination)
            modified
        }

    private fun generateBlurHash(image: ImmutableImage): String {
        val newBufferedImage = image.toNewBufferedImage(BufferedImage.TYPE_INT_RGB)
        return BlurHash.encode(newBufferedImage, 4, 4)
    }
}