package com.example.a438_project1
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Network {
    private val moshi = Moshi.Builder().build()

    private val logging = HttpLoggingInterceptor().apply{
        level = HttpLoggingInterceptor.Level.BASIC
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    /**
     * Our lyrics API
     */
    val lyricsApi: LyricsOvhApi = Retrofit.Builder()
        .baseUrl("https://api.lyrics.ovh/v1/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(LyricsOvhApi::class.java)
}