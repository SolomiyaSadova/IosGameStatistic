package com.ralabs.gamestatistic

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info

@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableScheduling
@OpenAPIDefinition(
		info = Info(
				title = "Game Top Charts Application",
				version = "1.0.0",
				contact = Contact(
						name = "Solomiya Sadova",
						email = "engineering@capitalise.com",
						url = "capitalise.com"
				)))
open class DemoApplication {

	@Bean
	open fun clientConnector() = ReactorClientHttpConnector(HttpClient.create().keepAlive(false))

	@Bean
	open fun webClient(clientConnector: ReactorClientHttpConnector): WebClient = WebClient.builder()
			.clientConnector(clientConnector)
			.build()

}

fun main(args: Array<String>) {
	SpringApplicationBuilder(DemoApplication::class.java)
			.properties("spring.config.name=application,fetchUrls")
			.run(*args)
}



