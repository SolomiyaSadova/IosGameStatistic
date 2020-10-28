package com.ralabs.gamestatistic.service.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.validation.constraints.NotEmpty

@JsonIgnoreProperties(ignoreUnknown = true)
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


