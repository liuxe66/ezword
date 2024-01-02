package com.atom.ezwords.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.atom.ezwords.R
import com.atom.ezwords.data.entity.Option
import com.atom.ezwords.ui.theme.ActBg
import com.atom.ezwords.ui.theme.Btnbg
import com.atom.ezwords.ui.theme.ErrorBg
import com.atom.ezwords.ui.theme.F5Bg
import com.atom.ezwords.ui.theme.RightBg
import com.atom.ezwords.ui.vm.ExamVM
import com.atom.ezwords.utils.logE
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 *  author : liuxe
 *  date : 2023/12/22 15:00
 *  description :
 */
@Composable
fun PlayScreen(controller: NavController, examVM: ExamVM) {

    var dialogState by remember {
        mutableStateOf(false)
    }

    BackHandler(enabled = true) {
        dialogState = true
        controller.popBackStack()
    }

    val uiController = rememberSystemUiController()
    SideEffect {
        uiController.setStatusBarColor(Color.Transparent)
        uiController.statusBarDarkContentEnabled = false
    }

    val examData by examVM.wordExamFlow.collectAsState()
    LaunchedEffect(Unit) {
        examVM.getExam(500, 1000)
    }

    LaunchedEffect(examVM.finished) {
        //完成
        if (examVM.finished) {
            controller.navigate("result?totalNum=${examVM.rank}") {
                popUpTo("play") { inclusive = true }
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ActBg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentAlignment = Alignment.TopCenter

            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_act_bg),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Text(
                    text = "${examVM.rank}",
                    modifier = Modifier.padding(top = 60.dp),
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.statusBarsHeight())

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 150.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 20.dp,
                                start = 24.dp,
                                end = 24.dp,
                            )
                            .background(
                                color = Color.White, shape = RoundedCornerShape(16.dp)
                            ), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(60.dp))
                        Text(
                            text = examData.correctMean,
                            color = Color.Black,
                            fontSize = 24.sp,
                            fontFamily = FontFamily.Default,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        examData.options.forEachIndexed { index, option ->
                            SelectionItem(option = option, index = index, examVM = examVM)
                        }

                        Surface(
                            color = F5Bg,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 5.dp)
                                .bounceClick()
                                .clickable(indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        examVM.onClickDont()
                                    })
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "不认识",
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 20.dp)
                                )
                            }

                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Text(
                        modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp),
                        text = "不确定的单词选择【不认识】会让测试更准确",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Default
                    )
                }

                Text(
                    text = "第${examVM.qNum}/${examVM.allQ}题",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default,
                    textAlign = TextAlign.Companion.Center,
                    modifier = Modifier
                        .bounceClick()
                        .fillMaxWidth()
                        .padding(horizontal = 100.dp, vertical = 0.dp)
                        .background(
                            color = Btnbg, shape = RoundedCornerShape(
                                topEnd = 16.dp,
                                bottomEnd = 40.dp,
                                topStart = 16.dp,
                                bottomStart = 40.dp
                            )
                        )
                        .padding(vertical = 16.dp)
                )

            }
        }

    }
}

@Composable
fun showBackDialog(dialogState: MutableState<Boolean>) {
    if (dialogState.value) {
        Dialog(
            onDismissRequest = {
                dialogState.value = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp), color = Color.White
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.TopCenter), text = "top"
                    )
                    Text("center")
                    Text(
                        modifier = Modifier.align(Alignment.BottomCenter), text = "bottom"
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun SelectionItem(
    option: Option = Option(txt = "hello", state = 1), index: Int = 1, examVM: ExamVM = viewModel()
) {
    Surface(
        color = when (option.state) {
            1 -> RightBg
            2 -> ErrorBg
            else -> F5Bg
        },
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .bounceClick()
            .clickable(indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    examVM.onClick(index)
                })
    ) {

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = option.txt,
                color = Color.Black,
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            )

//            Surface(
//                color = Color.Transparent,
//                shape = RoundedCornerShape(24.dp),
//                border = BorderStroke(1.dp, Color.Black),
//                modifier = Modifier
//                    .align(Alignment.CenterStart)
//                    .padding(start = 24.dp)
//            ) {
//                Text(
//                    text = when (index) {
//                        0 -> "A"
//                        1 -> "B"
//                        2 -> "C"
//                        3 -> "D"
//                        else -> "E"
//                    },
//                    color = Color.Black,
//                    fontSize = 18.sp,
//                    fontFamily = FontFamily.Monospace,
//                    fontWeight = FontWeight.Bold,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.size(24.dp)
//                )
//            }


            if (option.state != 0) {
                Image(
                    painter = when (option.state) {
                        1 -> painterResource(id = R.drawable.baseline_check_24)
                        2 -> painterResource(id = R.drawable.baseline_clear_24)
                        else -> painterResource(id = R.drawable.baseline_check_24)
                    },

                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 24.dp)
                )
            }

        }

    }

}