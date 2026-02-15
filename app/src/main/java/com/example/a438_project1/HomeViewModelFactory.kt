package com.example.a438_project1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HomeViewModelFactory(
    private val itunesApi: ItunesApi
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val artistRepo = ArtistRepository(itunesApi)
        return HomeViewModel(artistRepo) as T
    }
}