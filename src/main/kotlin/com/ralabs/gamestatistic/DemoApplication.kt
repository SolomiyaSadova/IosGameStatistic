package com.ralabs.gamestatistic

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableScheduling
open class DemoApplication {

	@Bean
	open fun clientConnector() = ReactorClientHttpConnector(HttpClient.create().keepAlive(false))

	@Bean
	open fun webClient(clientConnector: ReactorClientHttpConnector): WebClient = WebClient.builder()
			.clientConnector(clientConnector)
			.build()
}

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

