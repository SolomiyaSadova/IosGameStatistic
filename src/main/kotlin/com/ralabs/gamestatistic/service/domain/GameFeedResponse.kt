package com.ralabs.gamestatistic.listeners.domain

data class GameFeedResponse(
        val results: List<GameResponse> = emptyList()
)