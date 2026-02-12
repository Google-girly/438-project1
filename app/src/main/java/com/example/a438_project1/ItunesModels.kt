package com.example.a438_project1

data class ItunesSearchResponse(
    val resultCount: Int,
    val results: List<ItunesTrack>
)
data class ItunesTrack(
    val trackName: String?,
    val artistName: String?
)