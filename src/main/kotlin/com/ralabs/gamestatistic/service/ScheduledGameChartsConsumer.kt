package com.ralabs.gamestatistic.service

import com.ralabs.gamestatistic.config.ChartsConfiguration
import com.ralabs.gamestatistic.models.Game
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*

@Service
@Profile("app")
class ScheduledGameChartsConsumer(val feedClient: GamesFeedClient, val gameService: GameService,
                                  chartsConfiguration: ChartsConfiguration) {

    private val log: Logger = LoggerFactory.getLogger(ScheduledGameChartsConsumer::class.java)

    val urls: List<String> = Optional.ofNullable(chartsConfiguration.urls).orElse(emptyList())

    @Scheduled(fixedDelayString = "\${delay.before.feed.games.to.database}")
    fun consumeCharts(): Unit {
        log.info("Updating game charts in database...")
        urls.forEach { url ->
            feedClient
                    .fetchGames(url)
                    .map { response ->
                        val gameType = gameService.getGameTypeFromUrl(url)
                        Game.build(response, gameType)
                    }
                    .collectList()
                    .flatMapMany { games ->
                        gameService.saveGames(games)
                    }
                    .doOnComplete { log.info("Updated game charts in database from URL - $url") }
                    .subscribe()
        }
    }
}
