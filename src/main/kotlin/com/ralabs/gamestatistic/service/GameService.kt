package com.ralabs.gamestatistic.listeners

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ralabs.gamestatistic.models.Game
import com.ralabs.gamestatistic.models.GameType
import com.ralabs.gamestatistic.repository.GameRepository
import com.ralabs.gamestatistic.listeners.domain.GameFeedResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.io.File


@Service
class GameService(val gameRepository: GameRepository,
                  val objectMapper: ObjectMapper) {

    private val log: Logger = LoggerFactory.getLogger(GameService::class.java)

    fun saveGames(games: List<Game>): Flux<Game> = gameRepository.saveAll(games)

    fun getGames(limit: Long?): Flux<Game> = gameRepository.findAll().take(getOrDefaultLimit(limit))

    fun getGameTypeFromUrl(url: String): GameType {
        val gameType = url.toUpperCase().substringAfter("TOP-").substringBefore('/')
        return try {
            GameType.valueOf(gameType)
        } catch (ex: IllegalArgumentException) {
            GameType.ALL
        }
    }

    fun getGameByTypeAndLimit(gameType: GameType, limit: Long?): Flux<Game> =
            gameRepository.findByGameType(gameType).limitRequest(getOrDefaultLimit(limit))

    fun saveGamesFromFile(gameType: GameType): Unit {
        val classLoader = javaClass.classLoader
        val fileName = "${gameType.toString().toLowerCase()}-games-chart.json"
        val url = classLoader.getResource(fileName)
        if (url != null) {
            val file = File(url.file)
            readGamesFromFile(file, gameType)
                    .collectList()
                    .flatMapMany { saveGames(it) }
                    .doOnComplete { log.info("Updated game charts in database from $fileName") }
                    .doOnError { log.info("Can not get data from $fileName") }
                    .subscribe()
        }
    }

    fun readGamesFromFile(file: File, gameType: GameType): Flux<Game> {
        return Flux
                .fromIterable(objectMapper.readValue<GameFeedResponse>(file).results)
                .map { Game.build(it, gameType) }
    }

    private fun getOrDefaultLimit(limit: Long?): Long {
        val limitOrDefault = limit ?: DEFAULT_LIMIT
        return if (limitOrDefault == 0L) DEFAULT_LIMIT else limitOrDefault
    }

    companion object {
        const val DEFAULT_LIMIT: Long = 100
    }
}