package com.ralabs.gamestatistic.controller.rest

import com.ralabs.gamestatistic.models.Game
import com.ralabs.gamestatistic.models.GameType
import com.ralabs.gamestatistic.service.GameService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/v1/ios/charts/games")
class GameController(val gameService: GameService) {

    @GetMapping("/")
    fun getAllGames(@RequestParam(required = false) limit: Long?): Flux<Game>
            = gameService.getGames(limit)

    @GetMapping("/paid")
    fun getPaidGames(@RequestParam(required = false) limit: Long?): Flux<Game> =
            gameService.getGameByTypeAndLimit(GameType.PAID, limit)

    @GetMapping("/free")
    fun getFreeGames(@RequestParam(required = false) limit: Long?): Flux<Game> =
            gameService.getGameByTypeAndLimit(GameType.FREE, limit)
    
    @GetMapping("/grossing")
    fun getGrossingGames(@RequestParam(required = false) limit: Long?): Flux<Game> =
            gameService.getGameByTypeAndLimit(GameType.GROSSING, limit)

}
