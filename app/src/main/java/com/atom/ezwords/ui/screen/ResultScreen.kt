package com.atom.ezwords.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.atom.ezwords.ui.theme.ActBg
import com.atom.ezwords.ui.theme.Btnbg
import com.atom.ezwords.utils.DataStoreUtil
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *  author : liuxe
 *  date : 2023/12/22 09:52
 *  description :
 */
@Composable
fun ResultScreen(controller: NavController, totalNum: String, onShareClick: () -> Unit) {
    val uiController = rememberSystemUiController()
    SideEffect {
        uiController.setStatusBarColor(Color.Transparent)
        uiController.statusBarDarkContentEnabled = true

    }
    val lottieLuhua by rememberLottieComposition(LottieCompositionSpec.Asset("lt_lihua.json"))
    val lottieRibbon by rememberLottieComposition(LottieCompositionSpec.Asset("lt_ribbon.json"))


    var numStr = totalNum
    when (totalNum.length) {
        1 -> {
            numStr = "000${totalNum}"
        }

        2 -> {
            numStr = "00${totalNum}"
        }

        3 -> {
            numStr = "0${totalNum}"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ActBg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "您的词汇量",
            color = Color.Black,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
        )
        Spacer(modifier = Modifier.height(20.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NumView(numStr[0].toString().toInt())
            NumView(numStr[1].toString().toInt())
            NumView(numStr[2].toString().toInt())
            NumView(numStr[3].toString().toInt())
        }

        Box(
            modifier = Modifier.fillMaxWidth()

        ) {
            LottieAnimation(
                composition = lottieRibbon,
                isPlaying = true,
                iterations = Int.MAX_VALUE,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)


            )

            LottieAnimation(
                composition = lottieLuhua,
                isPlaying = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)


            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "分享",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default,
                textAlign = TextAlign.Companion.Center,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .background(
                        color = Color.White, shape = RoundedCornerShape(16.dp)
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onShareClick
                    )
                    .padding(vertical = 16.dp, horizontal = 24.dp)
            )
            Text(
                text = "重新测试",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default,
                textAlign = TextAlign.Companion.Center,
                modifier = Modifier
                    .background(
                        color = Btnbg, shape = RoundedCornerShape(16.dp)
                    )
                    .clickable(indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            controller.navigate("play") {
                                popUpTo("result") { inclusive = true }
                            }
                        })
                    .padding(vertical = 16.dp, horizontal = 48.dp)
            )
        }
    }

}

@Preview
@Composable
fun NumView(num: Int = 9) {

    var enable by remember { mutableStateOf(false) }
    val value by animateIntAsState(
        targetValue = if (enable) num else 0, animationSpec = tween(
            durationMillis = 500, delayMillis = 500, easing = LinearOutSlowInEasing
        ), label = ""
    )
    Box(
        modifier = Modifier
            .padding(2.dp)
            .size(70.dp)
            .background(color = Color.Black, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = value.toString(),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            onTextLayout = {
                enable = true
            })
    }
}
