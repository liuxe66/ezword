package com.atom.ezwords.ui.screen

import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.atom.ezwords.R
import com.atom.ezwords.utils.ImageProcessing
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

/**
 *  author : liuxe
 *  date : 2023/12/22 09:52
 *  description :
 */
@Composable
fun MuyuScreen(controller: NavController) {
    val uiController = rememberSystemUiController()
    val context = LocalContext.current
    SideEffect {
        uiController.setStatusBarColor(Color.Transparent)
        uiController.statusBarDarkContentEnabled = false
    }

    // 创建 SoundPool 对象
    val soundPool =
        SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).build()
            ).build()


    // 加载音效资源
    val soundId =
        soundPool.load(context, R.raw.muyuclick, 1)


    var gdnum by remember {
        mutableStateOf(0)
    }

    val animatedMuyuState = rememberAnimatedMuyuState()
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.ic_act_bg),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
        AnimatedMuyu(modifier = Modifier.fillMaxSize(),
            animatedMuyuState)
        Image(painter = painterResource(id = R.drawable.ic_muyu),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 160.dp, start = 100.dp, end = 100.dp)
                .align(Alignment.BottomCenter)
                .bounceClick {
                    coroutineScope.launch {
                        animatedMuyuState.onTap()
                        gdnum++
                        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
                    }
                })

//        LegoImage()
        Text(
            text = "功德：${gdnum}",
            modifier = Modifier
                .padding(top = 100.dp)
                .fillMaxWidth(),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
        )
    }

}

@Preview
@Composable
fun LegoImage(imageId: Int = R.drawable.ic_gender_woman) {


    // 加载图片
    val originalBitmap = BitmapFactory.decodeResource(LocalContext.current.resources, imageId)

    // 将图片转换为100x100的乐高风格的图片
    val legoBitmap = ImageProcessing.convertToLegoImage(originalBitmap)

    var imageBmp = legoBitmap.asImageBitmap()
    Column(modifier = Modifier.fillMaxSize()
        .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,) {

        Image(
            bitmap = originalBitmap.asImageBitmap(),
            contentDescription = "Original Image",
            modifier = Modifier
               .size(300.dp)
               .graphicsLayer(
                    alpha = 0.7f,
                    translationX = 10f,
                    translationY = 10f
                ),
            contentScale = ContentScale.Crop
        )
        Image(
           bitmap = imageBmp,
            contentDescription = "Lego Image",
            modifier = Modifier
                .size(300.dp)
                .graphicsLayer(
                    alpha = 0.7f,
                    translationX = 10f,
                    translationY = 10f
                ),
            contentScale = ContentScale.Crop
        )
    }
}

