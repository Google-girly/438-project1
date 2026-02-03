package com.example.a438_project1

import android.renderscript.Type
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

data class SpotifyTokenResponse (
    val access_token: String,
    val token_type: String,
    val expire_in: Int
    )
interface SpotifyAuthApi{
    @FormUrlEncoded
    @POST("api/token")
    suspend fun getToken(
        @Header("Authorization") basicAuth: String,
        @Field("grant_type") grantType: String= "client_credential"
    ): SpotifyTokenResponse
}