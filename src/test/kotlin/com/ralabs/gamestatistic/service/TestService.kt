package com.ralabs.gamestatistic.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ralabs.gamestatistic.models.Game
import com.ralabs.gamestatistic.models.GameType
import com.ralabs.gamestatistic.service.domain.*
import net.minidev.json.parser.JSONParser
import net.minidev.json.parser.ParseException
import org.junit.jupiter.api.Assertions
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.util.stream.Collectors

@Service
class TestService(
        val objectMapper: ObjectMapper,
        val gameService: GameService
){

    fun readGamesFromFile(queue: String): String {
        val classLoader = javaClass.classLoader
        val fileName = "${queue}-expected-message.json"
        val url = classLoader.getResource(fileName)
        if (url != null) {
            val file = File(url.file)

            println(readFileAsJson(queue))
            println(objectMapper.writeValueAsString(readFileAsJson(queue)))
            return objectMapper.writeValueAsString(readFileAsJson(queue))
        }
        return ""
    }

    fun changeOrderInList(games: List<Game>): MutableList<Game> {
        return gameService.changeGamesOrderInList(games as MutableList<Game>);
    }

    fun checkGeneratedInsights(newTopGameInsight: List<NewTopGameInsight>, newName: String,
                               changedRatingGameInsight: List<NewRatingGameInsight>, newRating: Int) {
        Assertions.assertNotNull(newTopGameInsight
                .stream()
                .filter { p -> p.gameGeneral.name == newName }
                .collect(Collectors.toList()))

        Assertions.assertNotNull(changedRatingGameInsight
                .stream()
                .filter { p -> p.gameGeneral.rating == newRating }
                .collect(Collectors.toList()))
    }

    fun readFromFileAndCreateListOfGames(fileName: String, gameType: GameType): List<Game> {
        return readFileAsJson(fileName)!!.results
    }

    private fun readFileAsJson(file: String): GameListResponse? {
        val parser = JSONParser()
        val fileName = "src/$file.json"
        try {
            val obj: Any = parser.parse(FileReader(fileName))
            val jsonObject = objectMapper.convertValue(obj, GameListResponse::class.java)
            return jsonObject
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }
}