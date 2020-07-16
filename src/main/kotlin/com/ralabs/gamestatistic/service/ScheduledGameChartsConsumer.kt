package com.ralabs.gamestatistic.service

import com.ralabs.gamestatistic.models.Game
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ScheduledGameChartsConsumer(val feedClient: GamesFeedClient, val gameService: GameService)
{

    var urls = listOf<String>("https://rss.itunes.apple.com/api/v1/us/ios-apps/top-free/games/100/explicit.json",
            "https://rss.itunes.apple.com/api/v1/us/ios-apps/top-paid/games/100/explicit.json",
            "https://rss.itunes.apple.com/api/v1/us/ios-apps/top-grossing/all/100/explicit.json")

    @Scheduled(fixedRate = 36000000)
    fun consumeCharts(): Unit =
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
                        .subscribe()

            }

}
