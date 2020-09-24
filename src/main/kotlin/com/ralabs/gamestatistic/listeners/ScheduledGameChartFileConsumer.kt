package com.ralabs.gamestatistic.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ralabs.gamestatistic.models.Game
import com.ralabs.gamestatistic.models.GameType
import com.ralabs.gamestatistic.service.domain.GameFeedResponse
import com.ralabs.gamestatistic.service.domain.GameResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.io.File


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
        for (gameType in GameType.values()) {
            if (gameType != GameType.UNKNOWN) gameService.saveGamesFromFile(gameType)
        }
    }
}