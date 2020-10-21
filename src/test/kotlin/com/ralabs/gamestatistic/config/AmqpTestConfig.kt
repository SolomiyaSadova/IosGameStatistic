package com.ralabs.gamestatistic.config

//import com.github.fridujo.rabbitmq.mock.MockConnectionFactory

import com.fasterxml.jackson.databind.ObjectMapper
import com.ralabs.gamestatistic.listener.TopGamesRequestReceiver
import com.ralabs.gamestatistic.service.GameService
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.test.RabbitListenerTest
import org.springframework.amqp.rabbit.test.TestRabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@RabbitListenerTest(capture=true)
internal class AmqpTestConfig {
    @Bean
    fun connectionFactory(): ConnectionFactory {
        return CachingConnectionFactory("localhost");
    }

    @Bean
    fun testRabbitTemplate(): TestRabbitTemplate {
        val rabbitTemplate = TestRabbitTemplate(connectionFactory())
        return rabbitTemplate;
    }

    @Bean
    fun rabbitAdmin(): RabbitAdmin {
        return RabbitAdmin(connectionFactory())
    }

    @Bean
    fun listener(gameService: GameService,
                 rabbitTemplate: RabbitTemplate, objectMapper: ObjectMapper): TopGamesRequestReceiver {
        return TopGamesRequestReceiver(gameService, rabbitTemplate, objectMapper)
    }

}