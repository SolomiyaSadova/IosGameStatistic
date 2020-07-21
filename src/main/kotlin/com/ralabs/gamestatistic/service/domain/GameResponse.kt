package com.ralabs.gamestatistic.service.domain

import javax.validation.constraints.NotEmpty

data class GameResponse(
        @get:NotEmpty
        val name: String,
        @get:NotEmpty
        val artistName: String,
        @get:NotEmpty
        val url: String,
        @get:NotEmpty
        val releaseDate: String
)
