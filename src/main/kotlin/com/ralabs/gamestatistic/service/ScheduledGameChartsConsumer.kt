package com.ralabs.gamestatistic.service

import com.ralabs.gamestatistic.config.ChartsConfiguration
import com.ralabs.gamestatistic.models.Game
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

@Service
@Profile("app")
class ScheduledGameChartsConsumer(val feedClient: GamesFeedClient, val gameService: GameService,
                                  chartsConfiguration: ChartsConfiguration) {

    private val log: Logger = LoggerFactory.getLogger(ScheduledGameChartsConsumer::class.java)

    val urls: List<String> = Optional.ofNullable(chartsConfiguration.urls).orElse(emptyList())

    val counter: AtomicInteger = AtomicInteger(0)


    @Scheduled(initialDelay = 5000, fixedDelayString = "\${delay.before.feed.games.to.database}")
    fun consumeCharts(): Unit {
        log.info("Updating game charts in database...")

        urls.forEach { url ->
            val gameType = gameService.getGameTypeFromUrl(url)
            var rating = 0
            feedClient
                    .fetchGames(url)
                    .map { response ->
                        rating++
                        Game.build(response, gameType, rating)
                    }
                    .collectList()
                    .flatMapMany { games ->
                        if (counter.incrementAndGet() % 2 == 0) {
                            gameService.changeGameInList(games)
                            gameService.changeGamesOrderInList(games)
                        }

                        gameService.saveGames(games, gameType)

                    }
                    .doOnComplete {
                        log.info("Updated game charts in database from URL - $url")
                    }
                    .subscribe()
        }
    }


}
