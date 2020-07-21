package com.ralabs.gamestatistic.models

import com.ralabs.gamestatistic.service.domain.GameResponse
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Document(collection = "games")
data class Game(
        @Id
        val id: String = UUID.randomUUID().toString(),
        val name: String = "",
        val artistName: String = "",
        val url: String = "",
        val releaseDate: LocalDateTime? = null,
        val gameType: GameType = GameType.UNKNOWN,
        @CreatedDate
        val createdAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {

        private const val RELEASE_DATE_PATTERN = "yyyy-MM-dd"
        private val dateTimeFormatter = DateTimeFormatter.ofPattern(RELEASE_DATE_PATTERN)

        fun build(dto: GameResponse, type: GameType): Game = Game(
                name = dto.name,
                artistName = dto.artistName,
                url = dto.url,
                releaseDate = parseReleaseDate(dto.releaseDate),
                gameType = type
        )

        private fun parseReleaseDate(releaseDate: String): LocalDateTime? =
                LocalDate.parse(releaseDate, dateTimeFormatter).atStartOfDay()

    }
}