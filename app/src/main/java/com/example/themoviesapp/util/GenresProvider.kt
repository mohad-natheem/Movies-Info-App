package com.example.themoviesapp.util

import androidx.compose.runtime.Composable
import com.example.themoviesapp.main.domain.model.Genre

@Composable
fun GenresProvider(
    genreIds: List<Int>,
    allGenres: List<Genre>
):String {
    var genres = ""
    for(i in genreIds.indices){
        for(j in allGenres.indices){
            if(allGenres[j].id == genreIds[i]){
                genres += "${allGenres[j].name}-"
            }
        }
    }

    return genres.dropLastWhile { it== ' ' ||it =='-' }
}