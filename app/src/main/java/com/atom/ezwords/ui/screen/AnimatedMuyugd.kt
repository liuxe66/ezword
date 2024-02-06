package com.atom.ezwords.ui.screen;

import android.content.res.Resources
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atom.ezwords.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.launch

/**
 * 木鱼功德 动画
 * @constructor
 */

@Composable
fun AnimatedMuyu(
    modifier: Modifier = Modifier,
    state: AnimatedMuyuState = rememberAnimatedMuyuState()
) {

    LaunchedEffect(Unit) {
        snapshotFlow { state.animatedMuyu.takeLast(1) }
            .flatMapMerge { it.asFlow() }.collect {
            launch {
                with(it) {
                    startAnim()
                    state.animatedMuyu.remove(it)
                }

            }
        }
    }
    val density = LocalDensity.current
    Canvas(modifier.fillMaxSize()) {
        state.animatedMuyu.forEach { animatable ->
            with(animatable) { draw(density) }
        }
    }
}



class AnimatedMuyugd(
    private val screenHeight: Float,
    private val screenWidth: Float,
    private val duration: Int = 1000,
    private val res: Resources,
    private val textMeasurer:TextMeasurer
) {

    private val textFont = Animatable(24f)

    private val y = Animatable(screenHeight / 2)

    private val alpha = Animatable(1f)

    suspend fun CoroutineScope.startAnim() {
        async {
            y.animateTo(
                (screenHeight / 2 - 400), animationSpec = tween(duration)
            )

        }
        awaitAll(async {
            textFont.animateTo(
                36f, animationSpec = tween(500)
            )
            textFont.animateTo(
                20f, animationSpec = tween(400)
            )
//            alpha.animateTo(
//                0f, animationSpec = tween(400)
//            )
        })
    }


    fun DrawScope.draw(density:Density) {

        var textWidth = with(density){
            textFont.value.sp.toPx() * "功德+1".length
        }

        drawText(
            textMeasurer,
            text = "功德+1",
            style = TextStyle(fontSize = textFont.value.sp, color = Color.White),
            topLeft = Offset(
                screenWidth / 2 - textWidth/2, y.value
            )
        )
    }
}

@Composable
fun rememberAnimatedMuyuState() = run {
    val screenHeightPx = LocalDensity.current.run {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }
    val screenWidthPx = LocalDensity.current.run {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }

    val res = LocalContext.current.resources
    val textMeasurer:TextMeasurer = rememberTextMeasurer()
    remember { AnimatedMuyuState(screenHeightPx, screenWidthPx,res,textMeasurer) }
}
class AnimatedMuyuState(
    private val screenHeight: Float,
    private val screenWidth: Float,
    private val res: Resources,
    private val textMeasurer:TextMeasurer
) {

    internal val animatedMuyu = mutableStateListOf<AnimatedMuyugd>()

    fun onTap() {
        animatedMuyu.addAll(buildList {
            add(AnimatedMuyugd(screenHeight, screenWidth, res = res, textMeasurer = textMeasurer))
        })
    }
}
