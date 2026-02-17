package com.example.a438_project1

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LyricsRepTest {

    private lateinit var server: MockWebServer
    private lateinit var api: LyricsOvhApi
    private lateinit var repo: LyricsRep

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        api = Retrofit.Builder()
            // simulating ".../v1/" baseUrl using MockWebServer
            .baseUrl(server.url("/v1/"))
            .addConverterFactory(GsonConverterFactory.create()) //uses Gson to convert JSON into Kotlin model
            .build()
            .create(LyricsOvhApi::class.java)

        repo = LyricsRep(api) //mock API into repo
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `fetchLyrics returns lyrics when API provides lyrics`() = runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("""{ "lyrics": "line1\nline2" }""")
        )

        val result = repo.fetchLyrics(song = "My Song", artist = "My Artist")
        assertEquals("line1\nline2", result)

        val req = server.takeRequest()
        assertEquals("/v1/My%20Artist/My%20Song", req.path)
    }

    @Test
    fun `fetchLyrics returns error field when lyrics is null`() = runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("""{ "error": "No lyrics found" }""")
        )

        val result = repo.fetchLyrics(song = "X", artist = "Y")
        assertEquals("No lyrics found", result) //returns error when lyric field missing
    }

    @Test
    fun `fetchLyrics sanitizes slashes in artist and song before building path`() = runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("""{ "lyrics": "ok" }""")
        )

        val result = repo.fetchLyrics(song = "So/ng", artist = "AC/DC")
        assertEquals("ok", result)

        val req = server.takeRequest()
        // Actual code: "AC/DC" → "AC DC"
        //               "So/ng" → "Song"
        assertEquals("/v1/AC%20DC/Song", req.path) //matching expected code syntax
    }
}
