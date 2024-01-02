package com.atom.ezwords.ui.screen

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraManager
import android.media.Image
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.atom.ezwords.ui.theme.ActBg
import com.atom.ezwords.ui.theme.Btnbg
import com.atom.ezwords.utils.ImageProcessing
import com.atom.ezwords.utils.ToastUtil
import com.atom.ezwords.utils.logE
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.LinkedList
import java.util.concurrent.atomic.AtomicBoolean

/**
 *  author : liuxe
 *  date : 2023/12/22 09:52
 *  description :
 */

val averageData = LinkedList<Int>()
var isStarting by mutableStateOf(false)
var endTime = 0L
var curTime by mutableStateOf(0L)
var resultStr by mutableStateOf("")
var camera:Camera? = null
var countRestart = 0

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HeartRateScreen(controller: NavController) {
    val uiController = rememberSystemUiController()
    SideEffect {
        uiController.setStatusBarColor(Color.Transparent)
        uiController.statusBarDarkContentEnabled = true
    }

    val cameraPermissionState =
        rememberPermissionState(permission = android.Manifest.permission.CAMERA)
    LaunchedEffect(key1 = Unit) {
        if (!cameraPermissionState.status.isGranted && !cameraPermissionState.status.shouldShowRationale) {
            cameraPermissionState.launchPermissionRequest()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "心率监测",
            color = Color.Black,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (cameraPermissionState.status.isGranted) {
            //接受拍照的授权
            CameraContent()
        } else {
            //未授权，显示未授权的界面
            NoPermissionScreen(cameraPermissionState)
        }

    }

}

fun reStart() {
    isStarting = true
    averageData.clear()
    endTime = 0L
    countRestart = 0
    curTime = 0L
    resultStr = ""
    camera?.cameraControl?.enableTorch(isStarting)
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NoPermissionScreen(cameraPermissionState: PermissionState) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val message = if (cameraPermissionState.status.shouldShowRationale) {
            "未获取照相机权限导致无法使用照相机功能"
        } else {
            "请授权照相机的权限"
        }
        Text(message)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            cameraPermissionState.launchPermissionRequest()
        }) {
            Text("请求授权")
        }
    }
}

@Composable
private fun CameraContent() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current


    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }


    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("heartrate.json"))




    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(160.dp), shape = RoundedCornerShape(80.dp)
        ) {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                AndroidView(factory = { context ->
                    val previewView = PreviewView(context).apply {
                        //设置布局宽度和高度占据全屏
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        //设置渲染的实现模式
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        //设置缩放方式
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }
                    val executor = ContextCompat.getMainExecutor(context)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                        val preview = Preview.Builder().build()

//                        val imageCapture = ImageCapture.Builder()
//                            .setFlashMode(if (isStarting) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF)
//                            .build()

                        //图像分析
                        val imageAnalysis = ImageAnalysis.Builder().build().also {
                            it.setAnalyzer(executor) { image ->
                                processImage(image)
                                image.close()
                            }
                        }

                        cameraProvider.unbindAll()
                        camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, preview,// imageCapture,
                            imageAnalysis
                        )
                        preview.setSurfaceProvider(previewView.surfaceProvider)
                    }, executor)
                    previewView
                }, modifier = Modifier.fillMaxSize())
                Text(
                    text = if (curTime in 1..10) curTime.toString() else "",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
            }

        }
        Text(
            text = "请用你的手指盖住后置摄像头",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Companion.Center,
            modifier = Modifier
                .padding(top = 30.dp)
                .padding(vertical = 16.dp, horizontal = 48.dp)
        )

        Text(
            text = resultStr,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Companion.Center,
            modifier = Modifier
                .padding(top = 30.dp)
                .padding(vertical = 16.dp, horizontal = 48.dp)
        )
        if (!isStarting) {
            Text(
                text = "点击开始",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default,
                textAlign = TextAlign.Companion.Center,
                modifier = Modifier
                    .padding(top = 30.dp)
                    .background(
                        color = Btnbg, shape = RoundedCornerShape(16.dp)
                    )
                    .clickable(indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            reStart()

                        })
                    .padding(vertical = 16.dp, horizontal = 48.dp)
            )
        } else {
            LottieAnimation(
                composition = composition,
                isPlaying = true,
                iterations = Int.MAX_VALUE,
                modifier = Modifier
                    .fillMaxWidth()
//                    .aspectRatio(1125f/600f)
                    .height(280.dp)
                    .padding(vertical = 48.dp)

            )
        }

    }


}

fun YUV420toNV21(image: Image): ByteArray? {
    val crop = image.cropRect
    val format = image.format
    val width = crop.width()
    val height = crop.height()
    val planes = image.planes
    val data = ByteArray(width * height * ImageFormat.getBitsPerPixel(format) / 8)
    val rowData = ByteArray(planes[0].rowStride)
    var channelOffset = 0
    var outputStride = 1
    for (i in planes.indices) {
        when (i) {
            0 -> {
                channelOffset = 0
                outputStride = 1
            }

            1 -> {
                channelOffset = width * height + 1
                outputStride = 2
            }

            2 -> {
                channelOffset = width * height
                outputStride = 2
            }
        }
        val buffer = planes[i].buffer
        val rowStride = planes[i].rowStride
        val pixelStride = planes[i].pixelStride
        val shift = if (i == 0) 0 else 1
        val w = width shr shift
        val h = height shr shift
        buffer.position(rowStride * (crop.top shr shift) + pixelStride * (crop.left shr shift))
        for (row in 0 until h) {
            var length: Int
            if (pixelStride == 1 && outputStride == 1) {
                length = w
                buffer[data, channelOffset, length]
                channelOffset += length
            } else {
                length = (w - 1) * pixelStride + 1
                buffer[rowData, 0, length]
                for (col in 0 until w) {
                    data[channelOffset] = rowData[col * pixelStride]
                    channelOffset += outputStride
                }
            }
            if (row < h - 1) {
                buffer.position(buffer.position() + rowStride - length)
            }
        }
    }
    return data
}


@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
private fun processImage(image: ImageProxy) {

    if (!isStarting) {
        return
    }

    val width: Int = image.width
    val height: Int = image.height
    var nv21 = YUV420toNV21(image.image!!)

    val imgAvg: Int = ImageProcessing.decodeYUV420SPtoRedAvg(
        nv21, height, width
    )
    "imgAvg：${imgAvg}".logE()

    if (imgAvg == 0 || imgAvg == 255 || imgAvg < 150) {
        countRestart++
        if (countRestart > 10) {
            isStarting = false
            camera?.cameraControl?.enableTorch(isStarting)
            return
        } else {
            reStart()
            return
        }
    } else {
        countRestart = 0
    }

    if (averageData.peekLast() == null || averageData.peekLast() != imgAvg) {
        averageData.add(imgAvg)
    }
    if (endTime == 0L) {
        endTime = System.currentTimeMillis() + 20000
        curTime = 0
    } else if (System.currentTimeMillis() < endTime) {
        curTime = (System.currentTimeMillis() - (endTime - 20000)) / 1000
    } else if (System.currentTimeMillis() >= endTime) {
        curTime = 0
        var result =
            "心脏跳动" + processData(averageData) + "次" + "，心率：" + processData(averageData) * 6
        result.logE()
        resultStr = "心率：" + processData(averageData) * 6
        isStarting = false
        camera?.cameraControl?.enableTorch(isStarting)
        return
    }
}

private fun processData(averageData: MutableList<Int>): Int {
    var dInt = 0
    var count = 0
    var isRise = false
    for (integer in averageData) {
        if (dInt == 0) {
            dInt = integer
            continue
        }
        if (integer > dInt) {
            if (!isRise) {
                count++
                isRise = true
            }
        } else {
            isRise = false
        }
        dInt = integer
    }
    return count
}

