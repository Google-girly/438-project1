package com.example.a438_project1

import com.example.a438_project1.ItunesApi
import com.example.a438_project1.ItunesRepository
import com.example.a438_project1.LyricsOvhApi
import com.example.a438_project1.LyricsRep
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiProvider {

    private val itunesRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val lyricsRetrofit: Retrofit by lazy {
        // Because your LyricsOvhApi uses @GET("{artist}/{title}")
        // baseUrl MUST end with /v1/
        Retrofit.Builder()
            .baseUrl("https://api.lyrics.ovh/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val itunesApi: ItunesApi by lazy { itunesRetrofit.create(ItunesApi::class.java) }
    val lyricsApi: LyricsOvhApi by lazy { lyricsRetrofit.create(LyricsOvhApi::class.java) }

    val itunesRepository: ItunesRepository by lazy { ItunesRepository(itunesApi) }
    val lyricsRep: LyricsRep by lazy { LyricsRep(lyricsApi) }
}
