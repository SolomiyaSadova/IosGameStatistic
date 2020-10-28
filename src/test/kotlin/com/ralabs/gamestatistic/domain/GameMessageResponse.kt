package com.ralabs.gamestatistic.domain

import com.ralabs.gamestatistic.models.Game

data class GameMessageResponse (
        val results: List<Game>
)