package com.example.themoviesapp.main.presentation

sealed class MainUiEvents {

    data class Refresh(val type: SCREENS): MainUiEvents()

    data class OnPaginate(val type: SCREENS): MainUiEvents()


}