package com.example.themoviesapp.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.themoviesapp.main.domain.model.Media
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoSwipeSection(
    modifier: Modifier = Modifier,
    mediaList: List<Media> = emptyList()
) {
    val pagerState = rememberPagerState{
        mediaList.size
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,

    ){
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = pagerState,
            pageSize = PageSize.Fill,
        ) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Radius.dp))
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .height(220.dp).
                        fillMaxWidth(0.9f)
                        .clip(RoundedCornerShape(Radius.dp))
                ){
                    Item(
                        modifier = Modifier
                            .fillMaxSize(),
                        media = mediaList[index],
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black
                                    ),
                                )
                            )
                            .padding(start = 12.dp, bottom = 12.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Text(
                            text = mediaList[index].title,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            LaunchedEffect(Unit) {
                while (true) {
                    if (pagerState.canScrollForward) {
                        delay(3000)
                        pagerState.animateScrollToPage(
                            page = pagerState.currentPage + 1,

                        )
                    } else {
                        pagerState.animateScrollToPage(
                            page = 0,
                        )
                    }
                }

            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        DotsIndicator(totalDots = mediaList.size, selectedIndex = pagerState.currentPage)
    }
    
}