package com.ralabs.gamestatistic.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ralabs.gamestatistic.models.GameType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service


@Service
@Profile("test")
class ScheduledGameChartFileConsumer(
        val objectMapper: ObjectMapper,
        val gameService: GameService
) {

    private val log: Logger = LoggerFactory.getLogger(ScheduledGameChartFileConsumer::class.java)


    @Scheduled(fixedDelayString = "\${delay.before.feed.games.to.database}")
    fun consumeCharts(): Unit {
        log.info("I am consuming charts from file")
        val fileName = "games-to-initialize-db.json"
        gameService.saveGamesFromFile(fileName, GameType.ALL)
    }
}