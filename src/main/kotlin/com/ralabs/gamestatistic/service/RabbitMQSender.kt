package com.ralabs.gamestatistic.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ralabs.gamestatistic.listener.domain.GameChartRequest
import com.ralabs.gamestatistic.models.GameType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class RabbitMQSender(
        val rabbitTemplate: RabbitTemplate,
        val objectMapper: ObjectMapper
) {

    private val log: Logger = LoggerFactory.getLogger(RabbitMQSender::class.java)

    @Value("\${spring.rabbitmq.template.exchange}")
    lateinit var exchange: String

    @Value("\${rabbitmq.queue.all.request}")
    lateinit var allGamesQueue: String

    @Value("\${rabbitmq.queue.grossing.request}")
    lateinit var grossingGamesQueue: String

    @Value("\${rabbitmq.queue.paid.request}")
    lateinit var paidGamesQueue: String

    @Value("{rabbitmq.queue.free.request}")
    lateinit var freeGamesQueue: String

    fun sendMessage(type: GameType, limit: Long): Unit {

        val message = convertToMessage(GameChartRequest(limit))
        when (type) {
            GameType.PAID -> {
                rabbitTemplate.convertAndSend(exchange, paidGamesQueue, message)
            }
            GameType.FREE -> {
                rabbitTemplate.convertAndSend(exchange, freeGamesQueue, message)
            }
            GameType.GROSSING -> {
                rabbitTemplate.convertAndSend(exchange, grossingGamesQueue, message)
            }
            GameType.ALL -> {
                rabbitTemplate.convertAndSend(exchange, allGamesQueue, message)
            }
        }

    }

    private fun convertToMessage(request: GameChartRequest): String =
            objectMapper.writeValueAsString(request)
}
