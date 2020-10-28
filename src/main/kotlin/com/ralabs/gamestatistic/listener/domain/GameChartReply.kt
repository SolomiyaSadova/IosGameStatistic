package com.ralabs.gamestatistic.listener.domain

import com.ralabs.gamestatistic.models.Game

data class GameChartReply (
    val games: List<Game>
)