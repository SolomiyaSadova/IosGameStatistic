package com.ralabs.gamestatistic.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ralabs.gamestatistic.listener.TopGamesRequestReceiver
import com.ralabs.gamestatistic.listener.domain.GameChartReply
import com.ralabs.gamestatistic.models.Game
import com.ralabs.gamestatistic.service.domain.GamesQueueMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class NewTopGamesPublisher(
        val rabbitTemplate: RabbitTemplate,
        val objectMapper: ObjectMapper
) {

    private val log: Logger = LoggerFactory.getLogger(NewTopGamesPublisher::class.java)

    @Value("\${spring.rabbitmq.template.exchange}")
    lateinit var exchange: String

    @Value("\${rabbitmq.queue.new-top-games}")
    lateinit var newTopGamesQueue: String

    @Value("\${rabbitmq.routingkey.new-top-games}")
    lateinit var newTopGamesRoutingKey: String

    fun sendMessageToQueue(message: String): Unit {
        rabbitTemplate.convertAndSend(exchange, newTopGamesRoutingKey, message)
        log.info("Send message to $newTopGamesQueue")
    }

    private fun writeMessage(gamesQueueMessage: GamesQueueMessage): String
            = objectMapper.writeValueAsString(gamesQueueMessage)

}