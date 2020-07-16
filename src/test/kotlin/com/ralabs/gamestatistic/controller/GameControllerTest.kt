package com.ralabs.gamestatistic.controller

import com.ralabs.gamestatistic.models.Game
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.concurrent.TimeUnit


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class GameControllerTest {

    private val EXPECTED_FETCH_TIME_IN_SECONDS: Long = 5L
    private val EXPECTED_TOTAL_NUMBER_OF_GAMES: Int = 300
    private val EXPECTED_TOTAL_NUMBER_OF_CERTAIN_GAMES_Type: Int = 100

    @LocalServerPort
    lateinit var port: Number
    lateinit var webTestClient: WebTestClient

    @BeforeAll
    fun setUp() {
        webTestClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:$port")
                .responseTimeout(Duration.ofMillis(20000))
                .build()
    }

    @BeforeEach
    fun waitUntilDataFetched() = TimeUnit.SECONDS.sleep(EXPECTED_FETCH_TIME_IN_SECONDS)

    @Test
    @DisplayName("should respond with list of all top chart games")
    fun getAllGamesTest() {
        val response = webTestClient
                .get()
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk
                .expectBodyList(Game::class.java)
                .returnResult().responseBody

        assertFalse(response == null)
        assertTrue(response!!.isNotEmpty())
        assertTrue(response.size == EXPECTED_TOTAL_NUMBER_OF_GAMES)
    }

    @Test
    @DisplayName("should respond with list of all paid games")
    fun getAllPaidGamesTest() {
        val response = webTestClient
                .get()
                .uri("/paid")
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk
                .expectBodyList(Game::class.java)
                .returnResult().responseBody

        assertFalse(response == null)
        assertTrue(response!!.isNotEmpty())
        assertTrue(response.size == EXPECTED_TOTAL_NUMBER_OF_CERTAIN_GAMES_Type)
    }

    @Test
    @DisplayName("should respond with list of all grossing games")
    fun getAllGrossingGamesTest() {
        val response = webTestClient
                .get()
                .uri("/grossing")
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk
                .expectBodyList(Game::class.java)
                .returnResult().responseBody

        assertFalse(response == null)
        assertTrue(response!!.isNotEmpty())
        assertTrue(response.size == EXPECTED_TOTAL_NUMBER_OF_CERTAIN_GAMES_Type)
    }
    @Test
    @DisplayName("should respond with list of all free games")
    fun getAllFreeGamesTest() {
        val response = webTestClient
                .get()
                .uri("/free")
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk
                .expectBodyList(Game::class.java)
                .returnResult().responseBody

        assertFalse(response == null)
        assertTrue(response!!.isNotEmpty())
        assertTrue(response.size == EXPECTED_TOTAL_NUMBER_OF_CERTAIN_GAMES_Type)
    }

    @Test
    @DisplayName("should respond with list of grossing games with some limit")
    fun getGrossinGamesWithLimitTest() {
        val response = webTestClient
                .get()
                .uri("/grossing/limit?limit={limit}", 10)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk
                .expectBodyList(Game::class.java)
                .returnResult().responseBody

        assertFalse(response == null)
        assertTrue(response!!.isNotEmpty())
        assertTrue(response.size == 10)

    }
    @Test
    @DisplayName("should respond with list of paid games with some limit")
    fun getPaidGamesWithLimitTest() {
        val response = webTestClient
                .get()
                .uri("/paid/limit?limit={limit}", 12)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk
                .expectBodyList(Game::class.java)
                .returnResult().responseBody

        assertFalse(response == null)
        assertTrue(response!!.isNotEmpty())
        assertTrue(response.size == 12)

    }

    @Test
    @DisplayName("should respond with list of free games with some limit")
    fun getFreeGamesWithLimitTest() {
        val response = webTestClient
                .get()
                .uri("/free/limit?limit={limit}", 2)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk
                .expectBodyList(Game::class.java)
                .returnResult().responseBody

        assertFalse(response == null)
        assertTrue(response!!.isNotEmpty())
        assertTrue(response.size == 2)

    }
}
