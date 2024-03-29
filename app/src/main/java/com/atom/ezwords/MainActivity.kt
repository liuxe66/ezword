package com.atom.ezwords

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.atom.ezwords.ui.screen.HomeScreen
import com.atom.ezwords.ui.screen.MuyuScreen
import com.atom.ezwords.ui.screen.PlayScreen
import com.atom.ezwords.ui.screen.ResultScreen
import com.atom.ezwords.ui.screen.ShopScreen
import com.atom.ezwords.ui.screen.VisionScreen
import com.atom.ezwords.ui.screen.count_money.CountMoneyScreen
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
                        composable("count_money"){
                            CountMoneyScreen(controller = navController)
                        }
                        composable("ShopScreen"){
                            ShopScreen()
                        }
                        composable("vision") {
                            VisionScreen(controller = navController)
                        }
                        composable("muyu") {
                            MuyuScreen(controller = navController)
                        }
                        composable("play") {
                            PlayScreen(controller = navController)
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
