package com.example.a438_project1

import org.w3c.dom.Entity
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {

    @GET("search")
    suspend fun searchSongs(
        @Query("term")term: String,
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int = 50
    ): ItunesSearchResponse
}