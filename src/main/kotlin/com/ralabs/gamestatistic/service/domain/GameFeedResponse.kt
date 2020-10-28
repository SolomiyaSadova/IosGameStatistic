package com.ralabs.gamestatistic.service.domain

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.ralabs.gamestatistic.models.Game

data class GameFeedResponse(
        val results: MutableList<GameResponse> = mutableListOf()
)

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class GameListResponse(
        val results: List<Game> = emptyList()
)