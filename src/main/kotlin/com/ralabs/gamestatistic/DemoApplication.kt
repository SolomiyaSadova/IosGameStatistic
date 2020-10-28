package com.ralabs.gamestatistic

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ralabs.gamestatistic.models.GameType
import com.ralabs.gamestatistic.service.GameService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.core.annotation.Order
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient


@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableScheduling
class DemoApplication {

    @Bean
    fun clientConnector() = ReactorClientHttpConnector(HttpClient.create().keepAlive(false))

    @Bean
    fun webClient(clientConnector: ReactorClientHttpConnector): WebClient = WebClient.builder()
            .clientConnector(clientConnector)
            .build()

    @Bean
    fun objectMapper(): ObjectMapper = jacksonObjectMapper()

}

fun main(args: Array<String>) {
    SpringApplicationBuilder(DemoApplication::class.java)
            .properties("spring.config.name=application,fetchUrls")
            .run(*args)
}

@Component
@Order(1)
class MyCommandLineRunner(
        val gameService: GameService
) : CommandLineRunner {

    private val log: Logger = LoggerFactory.getLogger(CommandLineRunner::class.java)

    override fun run(vararg args: String?) {
        for (gameType in GameType.values()) {
            val fileName = "${gameType.toString().toLowerCase()}-games-chart.json"
            if (gameType != GameType.ALL) gameService.saveGamesFromFile(fileName, gameType)
        }
        log.info("Stored games from file")

    }
}


