package com.atom.ezwords.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.atom.ezwords.ui.theme.ActBg
import com.atom.ezwords.ui.theme.Btnbg
import com.atom.ezwords.utils.DataStoreUtil
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 *  author : liuxe
 *  date : 2023/12/22 09:52
 *  description :
 */
@Composable
fun HomeScreen(controller: NavController) {

    val dataStore = DataStoreUtil(LocalContext.current)

    val score by dataStore.getScore.collectAsState(initial = "")


    val uiController = rememberSystemUiController()
    SideEffect {
        uiController.setStatusBarColor(Color.Transparent)
        uiController.statusBarDarkContentEnabled = true
    }
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("welcome.json"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ActBg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(120.dp))
        Text(
            text = "测试词汇量",
            color = Color.Black,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "快来测一下自己的单词量吧~",
            color = Color.Gray,
            fontSize = 16.sp,
            fontFamily = FontFamily.Default
        )

        LottieAnimation(
            composition = composition,
            isPlaying = true,
            iterations = Int.MAX_VALUE,
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .padding(vertical = 48.dp)

        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
           if (score.isNotBlank()) {
               Text(
                   text = "历史",
                   color = Color.Black,
                   fontSize = 20.sp,
                   fontWeight = FontWeight.Bold,
                   fontFamily = FontFamily.Default,
                   textAlign = TextAlign.Center,
                   modifier = Modifier
                       .padding(end = 16.dp)
                       .background(
                           color = Color.White, shape = RoundedCornerShape(16.dp)
                       )
                       .bounceClick()
                       .clickable(indication = null,
                           interactionSource = remember { MutableInteractionSource() },
                           onClick = {
                               controller.navigate("result?totalNum=${score}")
                           })
                       .padding(vertical = 16.dp, horizontal = 24.dp)
               )
           }

            Text(
                text = "开始测试",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(
                        color = Btnbg, shape = RoundedCornerShape(16.dp)
                    )
                    .bounceClick()
                    .clickable(indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            controller.navigate("play")
                        })
                    .padding(vertical = 16.dp, horizontal = 48.dp)
            )
        }

        Text(
            text = "想知道自己的视疲劳指数？",
            color = Color.Gray,
            fontSize = 16.sp,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .padding(top = 30.dp)
                .clickable(indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        controller.navigate("vision")
                    })
        )

        Text(
            text = "测心率？",
            color = Color.Gray,
            fontSize = 16.sp,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .padding(top = 30.dp)
                .clickable(indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        controller.navigate("heartRate")
                    })
        )
    }

}

enum class ButtonState { Pressed, Idle }

fun Modifier.bounceClick() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(
        if (buttonState == ButtonState.Pressed) 0.80f else 1f, label = ""
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { })
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}
