package com.ralabs.gamestatistic.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ralabs.gamestatistic.service.domain.GameTopInsight
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TopGamesInsightsPublisher(
        val rabbitTemplate: RabbitTemplate,
        val objectMapper: ObjectMapper
) {

    private val log: Logger = LoggerFactory.getLogger(TopGamesInsightsPublisher::class.java)

    @Value("\${spring.rabbitmq.template.exchange}")
    lateinit var exchange: String

    @Value("\${rabbitmq.queue.new-top-games}")
    lateinit var newTopGamesQueue: String

    @Value("\${rabbitmq.routingkey.new-top-games}")
    lateinit var newTopGamesRoutingKey: String

    fun publishInsight(insight: GameTopInsight): Unit {
        val message = writeMessage(insight)
        rabbitTemplate.convertAndSend(exchange, newTopGamesRoutingKey, message)
        log.info("Send message to $newTopGamesQueue")
    }

    private fun writeMessage(insight: GameTopInsight): String
            = objectMapper.writeValueAsString(insight)

}