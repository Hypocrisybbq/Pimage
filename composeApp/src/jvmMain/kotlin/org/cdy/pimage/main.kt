package org.cdy.pimage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.jetbrains.compose.resources.painterResource
import pimage.composeapp.generated.resources.Res
import pimage.composeapp.generated.resources.close
import pimage.composeapp.generated.resources.icon
import java.awt.Toolkit
import java.io.File
import javax.imageio.ImageIO
import org.jetbrains.skia.Image as SkiaImage

const val defaultImage3 = "C:\\Users\\chenc\\Desktop\\ccc.jpg"
fun main(images: Array<String>) = application {
    println("main")

    /** ------------------------ 获取窗口分辨率 ------------------------**/
    val (screenWidth, screenHeight) = getScreenSize()
    println("screenWidth: $screenWidth")
    println("screenHeight: $screenHeight")

    /** ------------------------ 获取图片分辨率和bitmap ------------------------**/
    val imageUrl = images.firstOrNull()
    val (imageWidth, imageHeight, imageBitmap) = getImageDetail(imageUrl)
    if (imageWidth < 0 || imageHeight < 0 || imageBitmap == null) {
        exitApplication()
    }

    /** ------------------------ 计算窗口大小 ------------------------**/
    val widthRatio: Double = if (imageWidth > screenWidth) {
        (imageWidth) / (screenWidth)
    } else {
        1.0
    }

    val heightRatio: Double = if (imageHeight > screenHeight) {
        (imageHeight) / (screenHeight)
    } else {
        1.0
    }

    val ratio = if (widthRatio > heightRatio) {
        widthRatio
    } else {
        heightRatio
    }

    val windowSize = DpSize(
        (imageWidth / ratio).toInt().dp,
        (imageHeight / ratio).toInt().dp
    )
    println("ratio: $ratio")
    println("windowSize: ${windowSize.width} : ${windowSize.height}")

    /** ------------------------ 窗口详情 ------------------------**/
    val windowState = rememberWindowState(
        position = WindowPosition(alignment = Alignment.Center),
        size = DpSize(windowSize.width + 48.dp, windowSize.height + 8.dp),
    )
    Window(
        state = windowState,
        alwaysOnTop = true,
        transparent = true,
        undecorated = true,
        resizable = false,
        onCloseRequest = ::exitApplication,
        title = "Pimage",
        icon = painterResource(Res.drawable.icon),
    ) {
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
            WindowDraggableArea(
                modifier = Modifier.fillMaxHeight().weight(1.0f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    imageBitmap?.let {
                        Image(it, "需要显示的图片", modifier = Modifier.fillMaxSize())
                    }
                }
            }
            Column(modifier = Modifier.size(40.dp)) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .clip(RoundedCornerShape(4.dp))
                        .background(iconBack)
                        .clickable { exitApplication() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(
                            resource = Res.drawable.close,
                        ),
                        contentDescription = "",
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

/**
 * 获取窗口分辨率
 */
fun getScreenSize(): Pair<Double, Double> {
    val toolkit = Toolkit.getDefaultToolkit()
    val screenSize = toolkit.screenSize
    return (screenSize.width) * 0.8 to (screenSize.height) * 0.8
}

/**
 * 获取图片分辨率以及bitmap
 */
fun getImageDetail(imageUrl: String?): Triple<Double, Double, ImageBitmap?> {
    try {
        val image: File = if (imageUrl.isNullOrEmpty()) {
            File(defaultImage3)
        } else {
            File(imageUrl)
        }
        val imageBuffer = ImageIO.read(image)

        val imageBytes = image.readBytes()
        val skiaImage = SkiaImage.makeFromEncoded(imageBytes)
        val imageBitmap = skiaImage.toComposeImageBitmap()
        imageBuffer?.let { buffer ->
            return Triple(buffer.width.toDouble(), buffer.height.toDouble(), imageBitmap)
        }
    } catch (e: Exception) {
        println(e)
        return Triple(-99.0, -99.0, null)
    }
    return Triple(-99.0, -99.0, null)
}

val iconBack = Color(0xFF2B2D30)