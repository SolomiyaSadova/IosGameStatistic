package com.ralabs.gamestatistic.controller

import com.ralabs.gamestatistic.models.Game
import com.ralabs.gamestatistic.models.GameType
import com.ralabs.gamestatistic.service.GameService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@ApiResponses(
        ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
        ApiResponse(responseCode = "500", description = "Internal Server Error", content = [Content()]))
@RestController
@RequestMapping("/v1/ios/charts/games")
class GameController(val gameService: GameService) {

    @Operation(summary = "Get all top games")
    @ApiResponse(responseCode = "200", description = "Successfully returned")
    @GetMapping("/")
    fun getAllGames(): Flux<Game> = gameService.getGames()

    @Operation(summary = "Get paid games")
    @ApiResponse(responseCode = "200", description = "Successfully returned")
    @GetMapping("/paid")
    fun getPaidGames(@RequestParam(required = false, defaultValue = LIMIT) limit: Long): Flux<Game> =
            gameService.getGameByTypeAndLimit(GameType.PAID, limit)

    @Operation(summary = "Get free games")
    @ApiResponse(responseCode = "200", description = "Successfully returned")
    @GetMapping("/free")
    fun getFreeGames(@RequestParam(required = false, defaultValue = LIMIT) limit: Long): Flux<Game> =
            gameService.getGameByTypeAndLimit(GameType.FREE, limit)

    @Operation(summary = "Get grossing games")
    @ApiResponse(responseCode = "200", description = "Successfully returned")
    @GetMapping("/grossing")
    fun getGrossingGames(@RequestParam(required = false, defaultValue = LIMIT) limit: Long): Flux<Game> =
            gameService.getGameByTypeAndLimit(GameType.GROSSING, limit)

    companion object {
        const val LIMIT: String = "100"
    }
}
