package com.ralabs.gamestatistic.repository

import com.ralabs.gamestatistic.models.Game
import com.ralabs.gamestatistic.models.GameType
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface GameRepository: ReactiveMongoRepository<Game, String> {
    fun findByGameType(gameType: GameType): Flux<Game>
    fun findByName(name: String): Mono<Game>
}