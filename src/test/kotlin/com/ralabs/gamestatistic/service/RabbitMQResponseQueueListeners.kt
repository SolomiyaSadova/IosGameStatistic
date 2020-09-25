package com.ralabs.gamestatistic.listeners

import com.ralabs.gamestatistic.listener.RabbitMQMessageReceiver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener

class RabbitMQResponseQueueListeners {
    private val log: Logger = LoggerFactory.getLogger(RabbitMQResponseQueueListeners::class.java)

    @RabbitListener(id = "all-games-response", queues = ["all-games-response-queue"])
    open fun handleGamesResponse(input: String): Unit {
        log.info(" [x] Received msg  from all-games-response-queue: '$input'.")
    }

    @RabbitListener(id = "paid-games-response", queues = ["paid-games-response-queue"])
    open fun handlePaidGamesResponse(input: String): Unit {
        log.info(" [x] Received msg  from paid-games-response-queue: '$input'.")
    }

    @RabbitListener(id = "free-games-response", queues = ["free-games-response-queue"])
    open fun handleFreeGamesResponse(input: String): Unit {
        log.info(" [x] Received msg  from free-games-response-queue: '$input'.")
    }

    @RabbitListener(id = "grossing-games-response", queues = ["grossing-games-response-queue"])
    open fun handleGrossingGamesResponse(input: String): Unit {
        log.info(" [x] Received msg  from grossing-games-response-queue: '$input'.")
    }
}