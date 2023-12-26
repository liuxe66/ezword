package com.atom.ezwords

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.atom.ezwords.ui.screen.HomeScreen
import com.atom.ezwords.ui.screen.PlayScreen
import com.atom.ezwords.ui.screen.ResultScreen
import com.atom.ezwords.ui.theme.EZwordsTheme
import com.atom.ezwords.ui.vm.ExamVM
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {

    private val examVM: ExamVM by viewModels()

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
                        composable("play") {
                            PlayScreen(controller = navController, examVM = examVM)
                        }
                        composable("result?totalNum={totalNum}",
                            arguments = listOf(navArgument("totalNum") {
                                type = NavType.StringType
                                defaultValue = "500"
                            })
                        ) {
                            ResultScreen(
                                controller = navController, it.arguments?.getString("totalNum")!!
                            )
                        }
                    }
                }
            }
        }
    }
}
