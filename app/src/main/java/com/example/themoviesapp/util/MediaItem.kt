package com.example.themoviesapp.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.themoviesapp.main.data.remote.api.MediaApi.Companion.IMAGE_BASE_URL
import com.example.themoviesapp.main.domain.model.Media
import com.example.themoviesapp.main.presentation.MainUiState
import com.example.themoviesapp.theme.font

@Composable
fun MediaItem(
    modifier: Modifier = Modifier,
    mainUiState: MainUiState,
    media: Media
) {
    val imageUrl = "${IMAGE_BASE_URL}${media.posterPath}"

    val title = media.title

    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val defaultDominantColor = MaterialTheme.colorScheme.secondaryContainer
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    val allGenres = if(media.mediaType == Constants.MOVIE){
        mainUiState.moviesGenreList
    }else{
        mainUiState.tvGenreList
    }

    val genres = GenresProvider(genreIds = media.genreIds, allGenres = allGenres)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.secondaryContainer,
                        dominantColor
                    )
                )
            )
            .padding(
                6.dp
            )


    ){
        Column(
            modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .height(240.dp)
                    .fillMaxWidth()
//                    .aspectRatio(
//                        0.7f,
//                        matchHeightConstraintsFirst = true
//                    )
//                    .clip(RoundedCornerShape(Radius.dp))
            ){
                when(imageState){
                    is AsyncImagePainter.State.Loading ->
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    is AsyncImagePainter.State.Success -> {
                        dominantColor = getAverageColor(imageBitmap = imageState.result.drawable.toBitmap().asImageBitmap())
                        Image(
                            bitmap = imageState.result.drawable.toBitmap().asImageBitmap(),
                            contentDescription = media.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(Radius.dp))
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
            Text(
                modifier = Modifier
                    .padding(horizontal = 12.dp,vertical = 4.dp),
                text = media.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = font,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                text = genres,
                fontSize = 12.5.sp,
                fontFamily = font,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
                )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                RatingBar(
                    starsModifier = Modifier.size(18.dp),
                    rating = media.voteAverage/2,
                    starsColor = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = media.voteAverage.toString().take(3),
                    fontSize = 14.sp,
                    fontFamily = font
                )
            }
        }
    }

}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun MediaItemPreview() {
    MediaItem(
        mainUiState = MainUiState(),
        media = Media(
            adult = false,
            backdropPath = "/xOMo8BRK7PfcJv9JCnx7s5hj0PX.jpg",
            genreIds = listOf(878, 12),
            id = 693134,
            originalLanguage = "en",
            originalTitle = "Dune: Part Two",
            overview = "Follow the mythic journey of Paul Atreides as he unites with Chani and the Fremen while on a path of revenge against the conspirators who destroyed his family. Facing a choice between the love of his life and the fate of the known universe, Paul endeavors to prevent a terrible future only he can foresee.",
            popularity = 2461.439,
            posterPath = "/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
            releaseDate = "2024-02-27",
            title = "Dune: Part Two",
            videos = emptyList(),
            voteAverage = 8.291,
            voteCount = 3062,
            category = "Popular",
            mediaType = "Movie",
            originCountry = listOf("USA"),
            runtime = 160,
            similarMediaList = emptyList(),
            status = "",
            tagline = ""
        )
    )
    
}