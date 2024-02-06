package com.atom.ezwords.ui.screen.count_money

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.atom.ezwords.R
import com.atom.ezwords.ui.theme.RightColor
import com.atom.ezwords.utils.logE
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.reflect.KProperty

/**
 *  author : liuxe
 *  date : 2023/12/22 09:52
 *  description :
 */
@Composable
fun CountMoneyScreen(controller: NavController) {
    val uiController = rememberSystemUiController()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    SideEffect {
        uiController.setStatusBarColor(Color.Transparent)
        uiController.statusBarDarkContentEnabled = false
    }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    var num by remember { mutableStateOf(0) }

    var scale by remember { mutableStateOf(1.5f) }
    val scaleAnimatableValue = remember { Animatable(scale) }


    LaunchedEffect(num) {
        scale = 1.5f
        delay(100)
        scaleAnimatableValue.animateTo(
            targetValue = 1f, animationSpec = tween(durationMillis = 200)
        ) {
            scale = value
        }

    }


    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = if (num == 0) "" else "x$num",
            color = if (num < 10) Color.Black else RightColor,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(top = 60.dp)
                .align(Alignment.TopCenter)
                .offset(x = 100.dp)
                .scale(scale)
        )

        if (num > 0) {
            Box(
                contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    Modifier
                        .offset {
                            IntOffset(0, -1500)
                        }
                        .scale(0.5f)) {
                    Image(
                        painter = painterResource(R.drawable.money),
                        contentDescription = "",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.width(180.dp)
                    )
                }
            }
        }



        for (i in 1..100) {
            DragGestureDemo(ok = {
                num++
            })
        }

    }

}

@Composable
fun DragGestureDemo(ok: () -> Unit) {
    var offsetY by remember { mutableStateOf(500) }
    var scale by remember { mutableStateOf(1f) }
    var isGoneAnimate by remember { mutableStateOf(false) }
    var isDragging by remember { mutableStateOf(false) }
    var canDrag by remember { mutableStateOf(true) }

    if (!isDragging) {
        if (isGoneAnimate) {
            val targetOffsetY = -1500f
            val offsetYAnimatableValue = remember { Animatable(offsetY.toFloat()) }
            val scaleAnimatableValue = remember { Animatable(scale) }
            LaunchedEffect(offsetYAnimatableValue) {
                async {
                    offsetYAnimatableValue.animateTo(
                        targetValue = targetOffsetY, animationSpec = tween(durationMillis = 100)
                    ) {
                        offsetY = value.roundToInt()
                    }
                }
                awaitAll(async {
                    scaleAnimatableValue.animateTo(
                        targetValue = 0.5f, animationSpec = tween(durationMillis = 100)
                    ) {
                        scale = value
                    }
                })
                canDrag = false
                ok.invoke()
            }

        } else {
            val targetOffsetY = 500f
            val animatableValue = remember { Animatable(offsetY.toFloat()) }
            LaunchedEffect(animatableValue) {
                animatableValue.animateTo(
                    targetValue = targetOffsetY, animationSpec = tween(durationMillis = 200)
                ) {
                    offsetY = value.roundToInt()
                }
            }
        }
    }


    if (canDrag) {

        Box(
            contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()
        ) {
            Box(
                Modifier
                    .offset {
                        IntOffset(0, offsetY)
                    }
                    .scale(scale)
                    .pointerInput(Unit) {

                        detectDragGestures(onDragStart = { offset ->
                            isDragging = true
                            // 拖动开始
                        }, onDragEnd = {
                            isDragging = false
                            // 拖动结束
                            isGoneAnimate = offsetY < 200

                        }, onDragCancel = {
                            // 拖动取消
                        }, onDrag = { change: PointerInputChange, dragAmount: Offset ->
                            // 拖动中
                            offsetY += (dragAmount.y).toInt()

                        })

                    }) {
                Image(
                    painter = painterResource(R.drawable.money),
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.width(180.dp)
                )
            }
        }

    }
}
