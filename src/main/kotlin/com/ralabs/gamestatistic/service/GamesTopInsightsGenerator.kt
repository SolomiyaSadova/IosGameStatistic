package com.ralabs.gamestatistic.service

import com.ralabs.gamestatistic.models.Game
import com.ralabs.gamestatistic.service.domain.GameTopInsight
import com.ralabs.gamestatistic.service.domain.NewTopGameInsight
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class GameTopInsightsGenerator {

    fun generateInsights(games: List<Game>): Flux<GameTopInsight> {
        return Flux.merge(
                findNewGamesInTop(games),
                findOrderChangesInTop(games)
        )
    }

    fun findNewGamesInTop(games: List<Game>): Flux<NewTopGameInsight> {
        return Flux.empty()
    }

    fun findOrderChangesInTop(games: List<Game>): Flux<GameTopInsight> {
        //TODO create domain for this method like OrderChangeTopGameInsight
        return Flux.empty()
    }
}