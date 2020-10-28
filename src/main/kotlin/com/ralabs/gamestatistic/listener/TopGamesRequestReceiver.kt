package com.ralabs.gamestatistic.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ralabs.gamestatistic.listener.domain.GameChartReply
import com.ralabs.gamestatistic.listener.domain.GameChartRequest
import com.ralabs.gamestatistic.models.GameType
import com.ralabs.gamestatistic.service.GameService
import com.ralabs.gamestatistic.models.Game
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import reactor.core.publisher.Flux
import java.util.stream.Collectors


open class TopGamesRequestReceiver(
        private val gameService: GameService,
        private val rabbitTemplate: RabbitTemplate,
        private val objectMapper: ObjectMapper
) {

    @Value("\${spring.rabbitmq.template.exchange}")
    lateinit var exchange: String

    @Value("\${rabbitmq.routingkey.all.games.response}")
    lateinit var allGamesResponseRoutingKey: String

    @Value("\${rabbitmq.routingkey.free.games.response}")
    lateinit var freeGamesResponseRoutingKey: String

    @Value("\${rabbitmq.routingkey.paid.games.response}")
    lateinit var paidGamesResponseRoutingKey: String

    @Value("\${rabbitmq.routingkey.grossing.games.response}")
    lateinit var grossingGamesResponseRoutingKey: String

    private val log: Logger = LoggerFactory.getLogger(TopGamesRequestReceiver::class.java)

    @RabbitListener(id = "all-games-request", queues = ["all-games-request-queue"])
    open fun handleGamesRequest(input: String): Unit {
        log.info(" [x] Received msg  from all-games-request-queue: '$input'.")
        val message = readInput(input)
        sendMessage(message, GameType.ALL, allGamesResponseRoutingKey)
    }

    @RabbitListener(id = "paid-games-request", queues = ["paid-games-request-queue"])
    open fun handlePaidGamesRequest(input: String): Unit {
        log.info(" [x] Received msg  from paid-games-request-queue: '$input'.")
        val message = readInput(input)
        sendMessage(message, GameType.PAID, paidGamesResponseRoutingKey)
    }

    @RabbitListener(id = "free-games-request", queues = ["free-games-request-queue"])
    open fun handleFreeGamesRequest(input: String): Unit {
        log.info(" [x] Received msg  from free-games-request-queue: '$input'.")
        val message = readInput(input)
        sendMessage(message, GameType.FREE, freeGamesResponseRoutingKey)
    }

    @RabbitListener(id = "grossing-games-request", queues = ["grossing-games-request-queue"])
    open fun handleGrossingGamesRequest(input: String) {
        log.info(" [x] Received msg  from grossing-games-request-queue: '$input'.")
        val message = readInput(input)
        sendMessage(message, GameType.GROSSING, grossingGamesResponseRoutingKey)
    }


    private fun readInput(input: String): GameChartRequest = objectMapper.readValue<GameChartRequest>(input)

    private fun writeOutput(reply: GameChartReply): String = objectMapper.writeValueAsString(reply)

    private fun sendMessage(message: GameChartRequest, gameType: GameType,
                            routingKey: String) {

        getGames(gameType, message.limit).collect(Collectors.toList())
                .map {
                    val reply = writeOutput(GameChartReply(it))
                    println("Reply - $reply")
                    Thread.sleep(5000)
                    rabbitTemplate.convertAndSend(exchange, routingKey, reply)
                }
                .doOnError {
                    log.error("Error during sending message to queue")
                }
                .subscribe {
                    log.info("Send message to " +
                            "\"${gameType.toString().toLowerCase()}-games-response-queue\" queue")
                }
    }

    private fun getGamesForAllGamesQueue(limit: Long): Flux<Game> {
        return gameService.getGames(limit)
    }

    private fun getGamesForTypedQueue(gameType: GameType, limit: Long): Flux<Game> {
        return gameService.getGameByTypeAndLimit(gameType, limit)
    }

    private fun getGames(gameType: GameType, limit: Long): Flux<Game> =
            if (gameType == GameType.ALL) {
                getGamesForAllGamesQueue(limit)
            } else {
                getGamesForTypedQueue(gameType, limit)
            }
}