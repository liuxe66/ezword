package com.atom.ezwords.ui.screen

import androidx.activity.compose.BackHandler
import androidx.activity.viewModels
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.atom.ezwords.R
import com.atom.ezwords.data.entity.Vision
import com.atom.ezwords.data.entity.VisionOption
import com.atom.ezwords.ui.theme.ActBg
import com.atom.ezwords.ui.theme.Btnbg
import com.atom.ezwords.ui.theme.RightBg
import com.atom.ezwords.ui.theme.RightColor
import com.atom.ezwords.ui.vm.VisionVM
import com.atom.ezwords.utils.logE
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

/**
 *  author : liuxe
 *  date : 2023/12/22 09:52
 *  description :
 */
@Composable
fun VisionScreen(controller: NavController) {
    val visionVM: VisionVM = viewModel()
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val uiController = rememberSystemUiController()
    SideEffect {
        uiController.setStatusBarColor(Color.Transparent)
        uiController.statusBarDarkContentEnabled = true
    }
    val visionData by visionVM.visionState.collectAsState()
    LaunchedEffect(Unit) {
        visionVM.initState()
        visionVM.getVision()
    }

    BackHandler(enabled = true) {
        controller.popBackStack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Spacer(
            modifier = Modifier
                .statusBarsHeight()
                .background(Color.White)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "视觉测试",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Cursive,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(ActBg), state = scrollState
        ) {
            item {
                StartItem(visionVM)
            }
            items(visionData.vision.size) { index ->
                QuestionItem(index, visionData.vision[index], visionVM)
            }

            if (visionVM.finished) {
                item {
                    ResultItem(visionVM)
                }
            }

            scope.launch {
                "scrollToItem".logE()
                scrollState.animateScrollToItem(
                    if (visionVM.finished) {
                        visionData.vision.lastIndex + 2
                    } else {
                        visionData.vision.lastIndex + 1
                    }

                )
            }

        }


    }


}

@Composable
fun ResultItem(visionVM: VisionVM) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_gender_woman),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(60.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 70.dp)
                    .background(color = Btnbg, shape = RoundedCornerShape(10.dp))
                    .align(Alignment.CenterVertically)
                    .padding(16.dp)
            ) {
                Text(
                    text = "您的视力测试得分：",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontFamily = FontFamily.Monospace,
                )
                Text(
                    text = "${visionVM.totalScore}分",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "结果：${visionVM.totalTitle}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .padding(top = 10.dp)
                )
                Text(
                    text = "${visionVM.totalResult}",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .padding(top = 10.dp)
                )
            }

        }


    }
}

@Preview
@Composable
fun StartItem(viewModel: VisionVM = VisionVM()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_gender_woman),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(60.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 70.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                    .align(Alignment.CenterVertically)
                    .padding(18.dp)
            ) {
                Text(
                    text = "眼睛好困总想闭上？\n" + "一闭眼就酸痛流泪？\n" + "这种现象在年轻人群体中已经非常常见，原因就是眼睛疲劳。\n" + "想知道自己的视疲劳指数是几级？那就来做几道有趣的测试题吧！",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,


                    )
                Box(
                    modifier = if (viewModel.isStart) {
                        Modifier
                            .padding(
                                top = 10.dp,
                            )
                            .fillMaxWidth()
                            .background(
                                color = ActBg, shape = RoundedCornerShape(10.dp)
                            )
                    } else {
                        Modifier
                            .padding(
                                top = 10.dp,
                            )
                            .fillMaxWidth()
                            .background(
                                color = Btnbg, shape = RoundedCornerShape(10.dp)
                            )
                            .bounceClick()
                            .clickable(indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    viewModel.start()
                                    viewModel.isStart = true
                                })
                    }

                ) {
                    Text(
                        text = "开始测试",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )

                }
            }

        }


    }
}

@Preview
@Composable
fun QuestionItem(
    qindex: Int = 0, data: Vision = Vision(
        question = "哪一处是你看书的理想位置？", options = mutableListOf(
            VisionOption(txt = "书桌"), VisionOption(txt = "沙发"), VisionOption(txt = "睡床")
        )
    ), viewModel: VisionVM = VisionVM()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_gender_woman),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(60.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 70.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                    .align(Alignment.CenterVertically)
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
            ) {

                Text(
                    text = data.question,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                        .padding(bottom = 16.dp)
                )

                data.options.forEachIndexed { index, visionOption ->
                    Box(
                        modifier = if (data.state == 2) {
                            Modifier
                                .padding(bottom = 10.dp)
                                .fillMaxWidth()
                                .background(
                                    color = if (data.options[index].state == 0) ActBg else RightBg,
                                    shape = RoundedCornerShape(10.dp)
                                )
                        } else {
                            Modifier
                                .padding(bottom = 10.dp)
                                .fillMaxWidth()
                                .bounceClick()
                                .background(
                                    color = if (data.options[index].state == 0) ActBg else RightBg,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable(indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        viewModel.onSelect(qindex, index)
                                    })
                        }

                    ) {
                        Text(
                            text = data.options[index].txt,
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        )
                    }
                }


            }

        }

        if (data.state == 2) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Spacer(
                    modifier = Modifier.size(60.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f, true),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 10.dp, start = 10.dp)
                            .background(color = RightColor, shape = RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = data.options[data.answer].txt,
                            modifier = Modifier.padding(16.dp),
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }


                Image(
                    painter = painterResource(id = R.drawable.ic_gender_man),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(60.dp)
                )
            }
        }


    }
}