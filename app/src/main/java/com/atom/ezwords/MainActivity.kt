package com.atom.ezwords

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.atom.ezwords.ui.screen.HeartRateScreen
import com.atom.ezwords.ui.screen.HomeScreen
import com.atom.ezwords.ui.screen.PlayScreen
import com.atom.ezwords.ui.screen.ResultScreen
import com.atom.ezwords.ui.screen.VisionScreen
import com.atom.ezwords.ui.theme.EZwordsTheme
import com.atom.ezwords.ui.vm.ExamVM
import com.atom.ezwords.ui.vm.VisionVM
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            EZwordsTheme {

                ProvideWindowInsets {
                    val uiController = rememberSystemUiController()
                    SideEffect {
                        uiController.setStatusBarColor(Color.Transparent)
                        uiController.statusBarDarkContentEnabled = true
                    }

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController, startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(controller = navController)
                        }
                        composable("heartRate") {
                            val visionVM: VisionVM by viewModels()
                            visionVM.initState()
                            HeartRateScreen(controller = navController)
                        }
                        composable("vision") {
                            val visionVM: VisionVM by viewModels()
                            visionVM.initState()
                            VisionScreen(controller = navController, visionVM = visionVM)
                        }
                        composable("play") {
                            val examVM: ExamVM by viewModels()
                            examVM.initState()
                            PlayScreen(controller = navController, examVM = examVM)
                        }
                        composable(
                            "result?totalNum={totalNum}",
                            arguments = listOf(navArgument("totalNum") {
                                type = NavType.StringType
                                defaultValue = "500"
                            })
                        ) {
                            ResultScreen(controller = navController,
                                it.arguments?.getString("totalNum")!!
                            ) {
                                gotoShare()
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 分享
     */
    fun gotoShare() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "词汇量")
        startActivity(Intent.createChooser(shareIntent, "分享到"))
    }
}
