package com.example.a438_project1

class ItunesRepository(private val api: ItunesApi) {
    suspend fun getFourSongs(artist: String): List<ItunesTrack> {
        val res = api.searchSongs(term = artist)

        val tracks =
            res.results.filter { !it.trackName.isNullOrBlank() && !it.artistName.isNullOrBlank() }

        val deduped = tracks.distinctBy { it.trackName!!.lowercase() }

        if (deduped.size < 4) throw IllegalStateException("Not enough songs for: $artist")

        return deduped.shuffled().take(4)
    }

}