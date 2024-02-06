package com.atom.ezwords.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.AddShoppingCart
import androidx.compose.material.icons.outlined.AutoAwesomeMosaic
import androidx.compose.material.icons.outlined.Snowshoeing
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 *  author : liuxe
 *  date : 2024/1/11 15:02
 *  description :
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShopScreen() {
    val pageState = rememberPagerState {
        2
    }
    val coroutineScope = rememberCoroutineScope()
    var curPage by remember {
        mutableStateOf(0)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pageState,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            curPage = pageState.currentPage
            when (index) {
                0 -> {
                    ShopHomeScreen()
                }

                1 -> {
                    ShopCartScreen()
                }
            }

        }


        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .clip(RoundedCornerShape(35.dp))
                .background(color = Color.Black)


        ) {

            Box(modifier = Modifier
                .clickable {
                    curPage = 0
                    coroutineScope.launch {
                        pageState.scrollToPage(curPage)
                    }
                }
                .padding(
                    top = 24.dp, start = 30.dp, bottom = 24.dp, end = 20.dp
                )) {

                Icon(
                    Icons.Outlined.AutoAwesomeMosaic,
                    contentDescription = "",
                    tint = if(curPage == 0) Color.White else Color.Gray,
                    modifier = Modifier.size(30.dp),
                )
            }

            Box(modifier = Modifier
                .clickable {
                    curPage = 1
                    coroutineScope.launch {
                        pageState.scrollToPage(curPage)
                    }
                }
                .padding(
                    top = 24.dp, start = 20.dp, bottom = 24.dp, end = 30.dp
                )) {

                Icon(
                    Icons.Outlined.AddShoppingCart,
                    contentDescription = "",
                    tint = if(curPage == 1) Color.White else Color.Gray,
                    modifier = Modifier.size(30.dp),
                )
            }


        }
    }


}

@Composable
fun ShopCartScreen() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red)
    ) {

    }
}


@Composable
fun ShopHomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Green)
    ) {

    }

}

@Preview
@Composable
fun ShopScreenPreview() {
    ShopScreen()
}