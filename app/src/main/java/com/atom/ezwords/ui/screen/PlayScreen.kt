package com.atom.ezwords.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
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
import com.atom.ezwords.utils.DataStoreUtil
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 *  author : liuxe
 *  date : 2023/12/22 15:00
 *  description :
 */
@Composable
fun PlayScreen(controller: NavController) {
    val examVM: ExamVM = viewModel()
    //当点击返回按钮时，询问是否弹窗
    var dialogState by remember {
        mutableStateOf(false)
    }

    //datastore保存数据
    val dataStore = DataStoreUtil(LocalContext.current)
    BackHandler(enabled = true) {
        dialogState = true
    }

    if (dialogState) {
        showBackDialog(onDimiss = {
            dialogState = false
        }, onOk = {
            dialogState = false
            controller.popBackStack()
        })
    }

    val uiController = rememberSystemUiController()
    SideEffect {
        uiController.setStatusBarColor(Color.Transparent)
        uiController.statusBarDarkContentEnabled = false
    }

    val examData by examVM.wordExamFlow.collectAsState()
    LaunchedEffect(Unit) {
        examVM.initState()
        examVM.getExam(500, 1000)
    }

    LaunchedEffect(examVM.finished) {
        //完成
        if (examVM.finished) {
            dataStore.saveScore(examVM.rank.toString())
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
                                .bounceClick {
                                    examVM.onClickDont()
                                }
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

@Preview
@Composable
fun showBackDialog(
    onDimiss: () -> Unit = {},
    onOk: () -> Unit = {},
) {

    Dialog(
        onDismissRequest = {
            onDimiss.invoke()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = true
        ),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White, shape = RoundedCornerShape(16.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier,
                text = "提示",
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
            )

            Text(
                modifier = Modifier.padding(24.dp),
                text = "测试还未完成，现在退出不保留数据，确认退出？",
                fontSize = 16.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 24.dp)
                        .clickable {
                            onDimiss.invoke()
                        },
                    text = "取消",
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "退出",
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onOk.invoke()
                        }
                        .padding(bottom = 24.dp))
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