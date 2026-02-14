package com.example.a438_project1.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.a438_project1.ItunesRepository
import com.example.a438_project1.LyricsRep

class GameViewModelFactory(
    private val itunesRepository: ItunesRepository,
    private val lyricsRep: LyricsRep
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(itunesRepository, lyricsRep) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


