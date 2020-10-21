package com.ralabs.gamestatistic.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class GamesQueueConfig {

    @Value("\${rabbitmq.queue.all.request}")
    lateinit var allGamesRequestQueue: String

    @Value("\${rabbitmq.queue.all.response}")
    lateinit var allGamesResponseQueue: String

    @Value("\${rabbitmq.queue.grossing.request}")
    lateinit var grossingGamesRequestQueue: String

    @Value("\${rabbitmq.queue.grossing.response}")
    lateinit var grossingGamesResponseQueue: String

    @Value("\${rabbitmq.queue.free.request}")
    lateinit var freeGamesRequestQueue: String

    @Value("\${rabbitmq.queue.free.response}")
    lateinit var freeGamesResponseQueue: String

    @Value("\${rabbitmq.queue.paid.request}")
    lateinit var paidGamesRequestQueue: String

    @Value("\${rabbitmq.queue.paid.response}")
    lateinit var paidGamesResponseQueue: String

    @Value("\${rabbitmq.queue.new-top-games}")
    lateinit var newTopGamesQueue: String

    @Value("\${rabbitmq.routingkey.all.games.request}")
    lateinit var allGamesRequestRoutingKey: String

    @Value("\${rabbitmq.routingkey.free.games.request}")
    lateinit var freeGamesRequestRoutingKey: String

    @Value("\${rabbitmq.routingkey.paid.games.request}")
    lateinit var paidGamesRequestRoutingKey: String

    @Value("\${rabbitmq.routingkey.grossing.games.request}")
    lateinit var grossingGamesRequestRoutingKey: String

    @Value("\${rabbitmq.routingkey.all.games.response}")
    lateinit var allGamesResponseRoutingKey: String

    @Value("\${rabbitmq.routingkey.free.games.response}")
    lateinit var freeGamesResponseRoutingKey: String

    @Value("\${rabbitmq.routingkey.paid.games.response}")
    lateinit var paidGamesResponseRoutingKey: String

    @Value("\${rabbitmq.routingkey.grossing.games.response}")
    lateinit var grossingGamesResponseRoutingKey: String

    @Value("\${rabbitmq.routingkey.new-top-games}")
    lateinit var newTopGamesRoutingKey: String

    @Bean
    fun queueAllGamesRequest(): Queue {
            return Queue(allGamesRequestQueue, true)
    }

    @Bean
    fun queueAllGamesResponse(): Queue {
        return Queue(allGamesResponseQueue, true)
    }

    @Bean
    fun queueGrossingGamesRequest(): Queue {
        return Queue(grossingGamesRequestQueue, true)
    }

    @Bean
    fun queueGrossingGamesResponse(): Queue {
        return Queue(grossingGamesResponseQueue, true)
    }

    @Bean
    fun queuePaidGamesRequest(): Queue {
        return Queue(paidGamesRequestQueue, true)
    }

    @Bean
    fun queuePaidGamesResponse(): Queue {
        return Queue(paidGamesResponseQueue, true)
    }

    @Bean
    fun queueFreeGamesRequest(): Queue {
        return Queue(freeGamesRequestQueue, true)
    }

    @Bean
    fun queueFreeGamesResponse(): Queue {
        return Queue(freeGamesResponseQueue, true)
    }

    @Bean
    fun newTopGames(): Queue {
        return Queue(newTopGamesQueue, true)
    }

    @Bean
    fun bindingAllGamesRequestQueue(@Qualifier("queueAllGamesRequest")
                                    queue: Queue?, exchange: DirectExchange?): Binding {
        return BindingBuilder.bind(queue).to(exchange).with(allGamesRequestRoutingKey)
    }

    @Bean
    fun bindingAllGamesResponseQueue(@Qualifier("queueAllGamesResponse")
                                     queue: Queue?, exchange: DirectExchange?): Binding {
        return BindingBuilder.bind(queue).to(exchange).with(allGamesResponseRoutingKey)
    }

    @Bean
    fun bindingFreeGamesRequestQueue(@Qualifier("queueFreeGamesRequest")
                                     queue: Queue?, exchange: DirectExchange?): Binding {
        return BindingBuilder.bind(queue).to(exchange).with(freeGamesRequestRoutingKey)
    }

    @Bean
    fun bindingFreeGamesResponseQueue(@Qualifier("queueFreeGamesResponse")
                                      queue: Queue?, exchange: DirectExchange?): Binding {
        return BindingBuilder.bind(queue).to(exchange).with(freeGamesResponseRoutingKey)
    }

    @Bean
    fun bindingPaidGamesRequestQueue(@Qualifier("queuePaidGamesRequest")
                                     queue: Queue?, exchange: DirectExchange?): Binding {
        return BindingBuilder.bind(queue).to(exchange).with(paidGamesRequestRoutingKey)
    }

    @Bean
    fun bindingPaidGamesResponseQueue(@Qualifier("queuePaidGamesResponse")
                                      queue: Queue?, exchange: DirectExchange?): Binding {
        return BindingBuilder.bind(queue).to(exchange).with(paidGamesResponseRoutingKey)
    }

    @Bean
    fun bindingGrossingGamesRequestQueue(@Qualifier("queueGrossingGamesRequest")
                                         queue: Queue?, exchange: DirectExchange?): Binding {
        return BindingBuilder.bind(queue).to(exchange).with(grossingGamesRequestRoutingKey)
    }

    @Bean
    fun bindingGrossingGamesResponseQueue(@Qualifier("queueGrossingGamesResponse")
                                          queue: Queue?, exchange: DirectExchange?): Binding {
        return BindingBuilder.bind(queue).to(exchange).with(grossingGamesResponseRoutingKey)
    }

    @Bean
    fun bindingNewTopGamesQueue(@Qualifier("newTopGames")
                                          queue: Queue?, exchange: DirectExchange?): Binding {
        return BindingBuilder.bind(queue).to(exchange).with(newTopGamesRoutingKey)
    }
}