package com.ralabs.gamestatistic.service

import com.ralabs.gamestatistic.models.Game
import com.ralabs.gamestatistic.models.GameType
import com.ralabs.gamestatistic.repository.GameRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux


@Service
class GameService(val gameRepository: GameRepository) {

    fun saveGames(games: List<Game>): Flux<Game> = gameRepository.saveAll(games)

    fun getGames(): Flux<Game> = gameRepository.findAll()

    fun enumContains(name: String): Boolean {
        return enumValues<GameType>().any { it.name == name }
    }

    fun getGameTypeFromUrl(url: String): GameType {
        var partsOfUrl = url.substringAfter("top-").substringBefore('/')
        var gameTypeToUpperCase = partsOfUrl.toUpperCase()
        return if (enumContains(gameTypeToUpperCase)) {
            GameType.valueOf(gameTypeToUpperCase)
        } else {
            GameType.UNKNOWN
        }
    }

}