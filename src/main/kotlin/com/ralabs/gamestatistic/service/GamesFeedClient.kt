package com.ralabs.gamestatistic.listeners

import com.ralabs.gamestatistic.listeners.domain.GameChartResponse
import com.ralabs.gamestatistic.listeners.domain.GameFeedResponse
import com.ralabs.gamestatistic.listeners.domain.GameResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Service
class GamesFeedClient(private val webClient: WebClient) {

    private val log: Logger = LoggerFactory.getLogger(GamesFeedClient::class.java)

    fun fetchGames(url: String): Flux<GameResponse> {
        log.info("Fetching games from URL - $url")
        return webClient.get().uri(url)
                .retrieve()
                .bodyToMono(GameChartResponse::class.java)
                .map(GameChartResponse::feed)
                .map(GameFeedResponse::results)
                .flatMapMany { Flux.fromIterable(it) }
                .doOnComplete { log.info("Fetched ios game charts from URL - $url") }
                .doOnError { log.error("Error during fetching ios game charts from URL - $url: $it") }
    }

}