package com.ralabs.gamestatistic.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.ralabs.gamestatistic.service.domain.GameGeneralInfo
import com.ralabs.gamestatistic.service.domain.GameResponse
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Document(collection = "games")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Game(
        @Id
        val id: String = UUID.randomUUID().toString(),
        var name: String = "",
        var artistName: String = "",
        val url: String = "",
        @JsonIgnore
        val releaseDate: LocalDateTime? = null,
        val gameType: GameType = GameType.ALL,
        @Min(1) @Max(100)
        var rating: Int = 0,
        @JsonIgnore
        @CreatedDate
        val createdAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {

        private const val RELEASE_DATE_PATTERN = "yyyy-MM-dd"
        private val dateTimeFormatter = DateTimeFormatter.ofPattern(RELEASE_DATE_PATTERN)

        fun build(dto: GameResponse, type: GameType, rating: Int): Game = Game(
                name = dto.name,
                artistName = dto.artistName,
                url = dto.url,
                releaseDate = parseReleaseDate(dto.releaseDate),
                gameType = type,
                rating = rating
        )

        fun toGameGeneralInfo(game: Game): GameGeneralInfo = GameGeneralInfo(
                name = game.name,
                artistName = game.artistName,
                releaseDate = game.releaseDate,
                rating = game.rating
        )

        private fun parseReleaseDate(releaseDate: String): LocalDateTime? =
                LocalDate.parse(releaseDate.toString(), dateTimeFormatter).atStartOfDay()
    }
}