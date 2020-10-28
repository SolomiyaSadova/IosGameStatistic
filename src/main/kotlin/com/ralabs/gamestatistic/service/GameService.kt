package com.ralabs.gamestatistic.service

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ralabs.gamestatistic.models.Game
import com.ralabs.gamestatistic.models.GameType
import com.ralabs.gamestatistic.repository.GameRepository
import com.ralabs.gamestatistic.service.domain.GameFeedResponse
import com.ralabs.gamestatistic.service.domain.GameListResponse
import kotlinx.coroutines.Dispatchers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.io.File
import java.io.FileWriter
import java.nio.file.Path
import java.nio.file.Paths


@Service
class GameService(val gameRepository: GameRepository,
                  val objectMapper: ObjectMapper,
                  val topGamesInsightsPublisher: TopGamesInsightsPublisher,
                  val gameTopInsightsGenerator: GameTopInsightsGenerator
) {

    private val ioDispatcher = Dispatchers.IO

    private val log: Logger = LoggerFactory.getLogger(GameService::class.java)

    fun saveGames(games: MutableList<Game>, gameType: GameType): Flux<Game> {

        generateAndPublishInsights(gameType, games)

        writeToFile(games, gameType)

        return gameRepository.saveAll(games)
    }

    fun getGames(limit: Long?): Flux<Game> = gameRepository.findAll().take(getOrDefaultLimit(limit?.times(3)))


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

    fun saveGamesFromFile(fileName: String, gameType: GameType): Unit {
        val classLoader = javaClass.classLoader
        val url = classLoader.getResource(fileName)
        if (url != null) {
            val file = File(url.file)
            readGamesFromFile(file)
                    .collectList()
                    .flatMapMany {
                        gameRepository.saveAll(it)
                    }
                    .doOnComplete { log.info("Updated game charts in database from $fileName") }
                    .doOnError { log.info("Can not get data from $fileName") }
                    .subscribe()
        }
    }

    fun readGamesFromFile(file: File): Flux<Game> {
        return Flux.fromIterable(objectMapper.readValue<GameListResponse>(file).results)
    }


    fun changeGameInList(games: MutableList<Game>) {
        val oneGame = games[4]
        val secondGame = games[3]
        oneGame.name = "Test_1_8 name"
        secondGame.name = "Test_2_8 name"
        games.removeAt(4)
        games.removeAt(3)
        games.add(3, oneGame)
        games.add(4, secondGame)
    }

    fun changeGamesOrderInList(games: MutableList<Game>): MutableList<Game> {
        val firstGame = games[0]
        val secondGame = games[1]
        val rating_1 = firstGame.rating
        val rating_2 = secondGame.rating
        firstGame.rating = rating_2
        secondGame.rating = rating_1
        games.removeAt(0)
        games.removeAt(1)
        games.add(0, firstGame)
        games.add(1, secondGame)
        return games
    }

    private fun getOrDefaultLimit(limit: Long?): Long {
        val limitOrDefault = limit ?: DEFAULT_LIMIT
        return if (limitOrDefault == 0L) DEFAULT_LIMIT else limitOrDefault
    }

    fun generateAndPublishInsights(gameType: GameType, nextTopGames: MutableList<Game>): Unit {

        getGameByTypeAndLimit(gameType, nextTopGames.size.toLong())
                .collectList()
                .map { savedGames ->
                    val insights = gameTopInsightsGenerator
                            .generateInsights(savedGames, nextTopGames)
                    insights.map { insight ->
                        topGamesInsightsPublisher.publishInsight(insight)
                    }
                }
                .subscribe()
    }

    private fun writeToFile(games: List<Game>, gameType: GameType): Unit {
        val fileName = "${gameType.toString().toLowerCase()}-games-chart.json"
        val path = "src/main/resources/$fileName"
        val gameListResponse: GameListResponse =
                objectMapper.readValue(File(path), GameListResponse::class.java)
        var jsonObject = JSONObject()
        jsonObject.put("results", games)
        val writer = FileWriter(path)
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        try {
            writer.write(objectMapper.writeValueAsString(GameListResponse(games)))
            log.info("Update file $fileName was successfully")
        } catch (e: Exception) {
            log.info("Can not update file $fileName. Error - ${e.message}")
        }
        writer.close()
    }


    companion object {
        const val DEFAULT_LIMIT: Long = 100
    }

}