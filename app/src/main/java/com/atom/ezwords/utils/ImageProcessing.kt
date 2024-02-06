package com.atom.ezwords.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.ContextCompat
import com.atom.ezwords.R

/**
 * 图像处理类
 */
object ImageProcessing {
    /**
     * 内部调用的处理图片的方法
     */
    private fun decodeYUV420SPtoRedSum(yuv420sp: ByteArray?, width: Int, height: Int): Int {
        if (yuv420sp == null) {
            return 0
        }
        val frameSize = width * height
        var sum = 0
        var j = 0
        var yp = 0
        while (j < height) {
            var uvp = frameSize + (j shr 1) * width
            var u = 0
            var v = 0
            var i = 0
            while (i < width) {
                var y = (0xff and yuv420sp[yp].toInt()) - 16
                if (y < 0) {
                    y = 0
                }
                if (i and 1 == 0) {
                    v = (0xff and yuv420sp[uvp++].toInt()) - 128
                    u = (0xff and yuv420sp[uvp++].toInt()) - 128
                }
                val y1192 = 1192 * y
                var r = y1192 + 1634 * v
                var g = y1192 - 833 * v - 400 * u
                var b = y1192 + 2066 * u
                if (r < 0) {
                    r = 0
                } else if (r > 262143) {
                    r = 262143
                }
                if (g < 0) {
                    g = 0
                } else if (g > 262143) {
                    g = 262143
                }
                if (b < 0) {
                    b = 0
                } else if (b > 262143) {
                    b = 262143
                }
                val pixel =
                    -0x1000000 or (r shl 6 and 0xff0000) or (g shr 2 and 0xff00) or (b shr 10 and 0xff)
                val red = pixel shr 16 and 0xff
                sum += red
                i++
                yp++
            }
            j++
        }
        return sum
    }

    /**
     * 对外开放的图像处理方法
     */
    fun decodeYUV420SPtoRedAvg(yuv420sp: ByteArray?, width: Int, height: Int): Int {
        if (yuv420sp == null) {
            return 0
        }
        val frameSize = width * height
        val sum = decodeYUV420SPtoRedSum(yuv420sp, width, height)
        return sum / frameSize
    }

    fun cropBitmapToSquare(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val minDimension = Math.min(width, height)

        val x = (width - minDimension) / 2
        val y = (height - minDimension) / 2

        val squareBitmap = Bitmap.createBitmap(minDimension, minDimension, bitmap.config)
        val canvas = Canvas(squareBitmap)
        val sourceRect = Rect(x, y, x + minDimension, y + minDimension)
        val destRect = Rect(0, 0, minDimension, minDimension)
        canvas.drawBitmap(bitmap, sourceRect, destRect, null)

        return squareBitmap
    }
    fun convertToLegoImage(originalBitmap: Bitmap): Bitmap {
        val width = 50
        val height = 50

        val squareBitmap = cropBitmapToSquare(originalBitmap)

        val scaledBitmap = Bitmap.createScaledBitmap(squareBitmap, width, height, true)

        // 创建一个新的空白Bitmap
        val legoBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // 创建一个Canvas，用于绘制乐高风格的图片
        val canvas = Canvas(legoBitmap)

        // 创建一个Paint对象，用于设置颜色和笔刷宽度
        val paint = Paint()
        paint.strokeWidth = 1f

        // 将原始图片转换为灰度图片
        val grayscaleBitmap = convertToGrayscale(scaledBitmap)

        // 遍历原始图片的每个像素
        for (y in 0 until height) {
            for (x in 0 until width) {
                // 获取原始图片的颜色值
                val color = grayscaleBitmap.getPixel(x, y)

                // 根据颜色值选择一个乐高风格的颜色
                val legoColor = when (color and 0xFF) {
                    in 0..32 -> Color.parseColor("#FF000000")
                    in 65..96 -> Color.parseColor("#FF444444")
                    in 97..160 -> Color.parseColor("#FF888888")
                    in 161..224 -> Color.parseColor("#FFCCCCCC")
                    in 225..255 -> Color.parseColor("#FFFFFFFF")
                    else -> Color.TRANSPARENT
                }

                // 设置画笔的颜色
                paint.color = legoColor

                // 绘制一个小方块
                canvas.drawRect(x.toFloat(), y.toFloat(), (x + 1).toFloat(), (y + 1).toFloat(), paint)
            }
        }

        return legoBitmap
    }

    fun convertToGrayscale(originalBitmap: Bitmap): Bitmap {
        val width = originalBitmap.width
        val height = originalBitmap.height

        // 创建一个新的空白Bitmap
        val grayscaleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // 创建一个Canvas，用于绘制灰度图片
        val canvas = Canvas(grayscaleBitmap)

        // 创建一个Paint对象，用于设置颜色和笔刷宽度
        val paint = Paint()
        paint.strokeWidth = 1f

        // 遍历原始图片的每个像素
        for (y in 0 until height) {
            for (x in 0 until width) {
                // 获取原始图片的颜色值
                val color = originalBitmap.getPixel(x, y)

                // 将颜色值转换为灰度值
                val grayscaleValue = (Color.red(color) * 0.299f + Color.green(color) * 0.587f + Color.blue(color) * 0.114f).toInt()

                // 设置画笔的颜色
                paint.color = Color.rgb(grayscaleValue, grayscaleValue, grayscaleValue)

                // 绘制一个小方块
                canvas.drawRect(x.toFloat(), y.toFloat(), (x + 1).toFloat(), (y + 1).toFloat(), paint)
            }
        }

        return grayscaleBitmap
    }
}