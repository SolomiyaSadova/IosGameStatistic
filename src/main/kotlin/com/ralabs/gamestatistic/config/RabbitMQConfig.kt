package com.ralabs.gamestatistic.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.ralabs.gamestatistic.service.GameService
import com.ralabs.gamestatistic.listener.TopGamesRequestReceiver
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    @Value("\${spring.rabbitmq.template.exchange}")
    lateinit var exchange: String

    @Bean
    fun exchange(): DirectExchange {
        return DirectExchange(exchange)
    }

    @Bean
    fun listener(gameService: GameService,
                        rabbitTemplate: RabbitTemplate, objectMapper: ObjectMapper): TopGamesRequestReceiver {
        return TopGamesRequestReceiver(gameService, rabbitTemplate, objectMapper)
    }

}