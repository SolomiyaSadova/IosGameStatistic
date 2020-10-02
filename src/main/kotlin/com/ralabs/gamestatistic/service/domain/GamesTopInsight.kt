package com.ralabs.gamestatistic.service.domain

import java.time.LocalDateTime

open class GameTopInsight(
        val generatedAt: LocalDateTime = LocalDateTime.now()
)

data class NewTopGameInsight(
        val isNew: Boolean,
        val game: GameResponse
) : GameTopInsight(LocalDateTime.now())
