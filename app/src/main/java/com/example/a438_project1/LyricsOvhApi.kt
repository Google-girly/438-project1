package com.example.a438_project1

import retrofit2.http.GET
import retrofit2.http.Path

interface LyricsOvhApi {

    @GET("{artist}/{title}")
    suspend fun getLyrics(
        @Path("artist") artist: String,
        @Path("title") title: String
    ): LyricsOvhResponse
}