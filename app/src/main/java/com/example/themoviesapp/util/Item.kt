package com.example.themoviesapp.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.themoviesapp.main.data.remote.api.MediaApi
import com.example.themoviesapp.main.domain.model.Media

@Composable
fun Item(
    modifier: Modifier = Modifier,
    media: Media
) {
    val imageUrl = "${MediaApi.IMAGE_BASE_URL}${media.posterPath}"

    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .build()
    ).state
    val defaultDominantColor = MaterialTheme.colorScheme.primaryContainer
    val dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Radius.dp))
            .background(dominantColor)
    ){
        when(imageState){
            is AsyncImagePainter.State.Loading ->
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.Center)
                )
            is AsyncImagePainter.State.Success -> {
                Image(
                    bitmap = imageState.result.drawable.toBitmap().asImageBitmap(),
                    contentDescription = media.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                    )
            }
            else -> {
                Icon(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = "error",
                    tint = MaterialTheme.colorScheme.onBackground
                    )
            }
        }
    }




}