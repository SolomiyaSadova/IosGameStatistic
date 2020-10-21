package com.ralabs.gamestatistic.service.domain

import java.time.LocalDateTime

data class GameGeneralInfo(
        val name: String,
        val artistName: String,
        val rating: Int,
        val releaseDate: LocalDateTime?
)