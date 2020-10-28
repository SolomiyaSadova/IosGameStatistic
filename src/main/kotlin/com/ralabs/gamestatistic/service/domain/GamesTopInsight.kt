package com.ralabs.gamestatistic.service.domain

import java.time.LocalDate
import java.time.LocalDateTime

open class GameTopInsight(
        val generatedAt: LocalDate = LocalDate.now()
)

data class NewTopGameInsight(
        val isNew: Boolean,
        val gameGeneral: GameGeneralInfo
) : GameTopInsight(LocalDate.now())

data class NewRatingGameInsight(
        val oldRating: Int,
        val newRating: Int,
        val gameGeneral: GameGeneralInfo
) : GameTopInsight(LocalDate.now())
