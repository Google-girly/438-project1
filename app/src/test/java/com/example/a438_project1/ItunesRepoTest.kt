package com.example.a438_project1

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItunesRepositoryTest {

    private lateinit var server: MockWebServer
    private lateinit var api: ItunesApi
    private lateinit var repo: ItunesRepository
    private val gson: Gson = GsonBuilder().create()

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        api = Retrofit.Builder()
            .baseUrl(server.url("/")) // Mock server base url
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ItunesApi::class.java)

        repo = ItunesRepository(api)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getFourSongs returns 4 unique, valid songs when API returns enough`() = runTest {
        val body = """
            {
              "resultCount": 6,
              "results": [
                { "trackName": "Song A", "artistName": "Artist" },
                { "trackName": "Song B", "artistName": "Artist" },
                { "trackName": "Song C", "artistName": "Artist" },
                { "trackName": "Song D", "artistName": "Artist" },
                { "trackName": "Song E", "artistName": "Artist" },
                { "trackName": "Song F", "artistName": "Artist" } 
              ]
            } 
        """.trimIndent()

        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val result = repo.getFourSongs("Artist") //artist name all the same so we
                                                        // can use getFourSongs and check at once

        // Bc repo shuffles, don't assert order.
        assertEquals(4, result.size)
        assertTrue(result.all { !it.trackName.isNullOrBlank() && !it.artistName.isNullOrBlank() })

        // Ensure they are unique by lowercase track name
        val namesLower = result.map { it.trackName!!.lowercase() }
        assertEquals(namesLower.toSet().size, namesLower.size)
    }

    @Test
    fun `getFourSongs dedupes tracks by lowercase name`() = runTest {
        val body = """
            {
              "resultCount": 6,
              "results": [
                { "trackName": "Hello", "artistName": "Artist" },
                { "trackName": "HELLO", "artistName": "Artist" },
                { "trackName": "World", "artistName": "Artist" },
                { "trackName": "Song 3", "artistName": "Artist" },
                { "trackName": "Song 4", "artistName": "Artist" },
                { "trackName": "Song 5", "artistName": "Artist" }
              ]
            }
        """.trimIndent()

        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val result = repo.getFourSongs("Artist") //again validating all artist names
        assertEquals(4, result.size)

        // ensures unique by lowercase track name
        val namesLower = result.map { it.trackName!!.lowercase() }
        assertEquals(namesLower.toSet().size, namesLower.size) // no duplicates
    }

    @Test
    fun `getFourSongs throws when fewer than 4 after filtering and dedupe`() = runTest {
        val body = """
            {
              "resultCount": 5,
              "results": [
                { "trackName": "A", "artistName": "Artist" },
                { "trackName": "A", "artistName": "Artist" },
                { "trackName": " ", "artistName": "Artist" },
                { "trackName": null, "artistName": "Artist" },
                { "trackName": "B", "artistName": null }
              ]
            }
        """.trimIndent()

        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        try {
            repo.getFourSongs("Artist")
            fail("Expected IllegalStateException")
        } catch (e: IllegalStateException) {
            assertTrue(e.message!!.contains("Not enough songs"))
        }
    }
}
