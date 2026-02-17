package com.example.a438_project1

class ArtistRepository(private val api: ItunesApi) {

    // Uses your existing searchSongs() endpoint and derives artists from returned tracks
    suspend fun loadArtistsFromApi(): List<String> {
        val seeds = listOf("pop", "rap", "rock", "r&b", "country")

        val set = linkedSetOf<String>()
        for (seed in seeds) {
            val res = api.searchSongs(term = seed)
            res.results
                .mapNotNull { it.artistName }
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .forEach { set.add(it) }
        }

        return set.toList().sorted()
    }
}