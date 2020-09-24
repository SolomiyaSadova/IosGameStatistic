package com.ralabs.gamestatistic.service

import com.ralabs.gamestatistic.models.GameType
import com.ralabs.gamestatistic.service.domain.GameChartRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

class RabbitMQMessageReceiver(
        private val gameService: GameService,
        private val rabbitTemplate: RabbitTemplate
) {

    @Value("\${sample.rabbitmq.exchange}")
    lateinit var exchange: String

    @Value("\${rabbitmq.routingkey.all.games.response}")
    lateinit var allGamesResponseRoutingKey: String

    @Value("\${rabbitmq.routingkey.free.games.response}")
    lateinit var freeGamesResponseRoutingKey: String

    @Value("\${rabbitmq.routingkey.paid.games.response}")
    lateinit var paidGamesResponseRoutingKey: String

    @Value("\${rabbitmq.routingkey.grossing.games.response}")
    lateinit var grossingGamesResponseRoutingKey: String

    private val log: Logger = LoggerFactory.getLogger(RabbitMQMessageReceiver::class.java)

    @RabbitListener(id = "all-games-request", queues = ["all-games-request-queue"])
    fun sendAllGamesInQueue(input: GameChartRequest): Unit {
        val games = gameService.getGames(input.limit);
        rabbitTemplate.convertAndSend(exchange, allGamesResponseRoutingKey, games)
        log.info(" [x] Received msg '$input'. Send message to \"all-games-response-queue\" queue")
    }

    @RabbitListener(id = "free-games-request",queues = ["free-games-request-queue"])
    fun sendFreeGamesInQueue(input: String) {
        val limit = getLimitFromMessageString(input)
        val games = gameService.getGameByTypeAndLimit(GameType.FREE, limit.toLong())
        rabbitTemplate.convertAndSend(exchange, freeGamesResponseRoutingKey, games)
        println(" [x] Received msg '$input'. Send message to \"free-games-response-queue\" queue")
    }

    @RabbitListener(id = "paid-games-request",queues = ["paid-games-request-queue"])
    fun sendPaidGamesInQueue(input: String) {
        val limit = getLimitFromMessageString(input)
        val games = gameService.getGameByTypeAndLimit(GameType.PAID, limit.toLong())
        rabbitTemplate.convertAndSend(exchange, paidGamesResponseRoutingKey, games)
        println(" [x] Received msg '$input'. Send message to \"paid-games-response-queue\" queue")
    }

    @RabbitListener(id = "grossing-games-request", queues = ["grossing-games-request-queue"])
    fun sendGrossingGamesInQueue(input: String) {
        val limit = getLimitFromMessageString(input)
        val games = gameService.getGameByTypeAndLimit(GameType.GROSSING, limit.toLong())
        rabbitTemplate.convertAndSend(exchange, grossingGamesResponseRoutingKey, games)
        println(" [x] Received msg '$input'. Send message to \"grossing-games-response-queue\" queue")
    }

    private fun getLimitFromMessageString(str: String): String {
        return str.substring(str.length - 1)
    }
}