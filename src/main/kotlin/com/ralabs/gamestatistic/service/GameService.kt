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

    fun getGameTypeFromUrl(url: String): GameType {
        val gameType = url.toUpperCase().substringAfter("TOP-").substringBefore('/')
        return try {
            GameType.valueOf(gameType)
        } catch (ex: IllegalArgumentException) {
            GameType.UNKNOWN
        }
    }

    fun getGameByTypeAndLimit(gameType: GameType, limit: Long): Flux<Game>
        = gameRepository.findByGameType(gameType).limitRequest(limit)
}