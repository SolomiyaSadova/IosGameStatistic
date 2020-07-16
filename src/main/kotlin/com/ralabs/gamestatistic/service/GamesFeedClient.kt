package com.ralabs.gamestatistic.service

import com.ralabs.gamestatistic.service.domain.GameChartResponse
import com.ralabs.gamestatistic.service.domain.GameFeedResponse
import com.ralabs.gamestatistic.service.domain.GameResponse
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Service
class GamesFeedClient(private val webClient: WebClient) {

    fun fetchGames(url: String): Flux<GameResponse> =
            webClient.get().uri(url)
                    .retrieve()
                    .bodyToMono(GameChartResponse::class.java)
                    .map(GameChartResponse::feed)
                    .map(GameFeedResponse::results)
                    .flatMapMany { Flux.fromIterable(it) }
}