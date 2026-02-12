package com.example.a438_project1
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object Network {
    private val moshi = Moshi.Builder().build()

    private val logging = HttpLoggingInterceptor().apply{
        level = HttpLoggingInterceptor.Level.BASIC
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    /**
     * Our lyrics API
     */
    val lyricsApi: LyricsOvhApi = Retrofit.Builder()
        .baseUrl("https://api.lyrics.ovh/v1/")
        .client(client)
        //.addConverterFactory(MoshiConverterFactory.create(moshi))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(LyricsOvhApi::class.java)
    //iTunes search
    val itunesApi: ItunesApi = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ItunesApi::class.java)
}