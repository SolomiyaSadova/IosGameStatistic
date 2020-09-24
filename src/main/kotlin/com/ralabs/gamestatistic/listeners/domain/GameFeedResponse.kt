package com.ralabs.gamestatistic.service.domain

data class GameFeedResponse(
        val results: List<GameResponse> = emptyList()
)