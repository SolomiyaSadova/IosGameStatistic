package com.ralabs.gamestatistic.controller

import com.ralabs.gamestatistic.models.Game
import com.ralabs.gamestatistic.models.GameType
import com.ralabs.gamestatistic.repository.GameRepository
import com.ralabs.gamestatistic.service.GameService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class GameController(val gameService: GameService, val gameRepository: GameRepository) {

    @GetMapping("/")
    fun getAllGames(): Flux<Game> = gameService.getGames()

    @GetMapping("/paid")
    fun getPaidGamesWithLimit(@RequestParam(required = false, defaultValue = "100") limit: Long) = gameRepository
            .findByGameType(GameType.PAID).limitRequest(limit)

    @GetMapping("/free")
    fun getFreeGamesWithLimit(@RequestParam(required = false, defaultValue = "100") limit: Long) = gameRepository
            .findByGameType(GameType.FREE).limitRequest(limit)

    @GetMapping("/grossing")
    fun getGrossingGamesWithLimit(@RequestParam(required = false, defaultValue = "100") limit: Long) = gameRepository
            .findByGameType(GameType.GROSSING).limitRequest(limit)
}