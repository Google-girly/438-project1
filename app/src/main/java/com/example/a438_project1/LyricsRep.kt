package com.example.a438_project1

class LyricsRep(private val api: LyricsOvhApi){
    /**
     * Code task:
     * Fetch Lyrics for song and artist
     * this is a suspend function, must run in a coroutine
     */
    suspend fun fetchLyrics(song: String, artist: String): String{
        /**
         * If by anychange an artist has the characted / on there name it would break the url path
         * so to prevent this we can just replace the character with a space
         */
        val safeArtists = artist.replace("/"," ")
        val safeSong = song.replace("/","")
        //make network call
        //GET
        val res = api.getLyrics(artist = safeArtists, title = safeSong)
        //error
        return res.lyrics
            ?: res.error
            ?:"No Lyrics found for: $song- $artist"
    }
}

