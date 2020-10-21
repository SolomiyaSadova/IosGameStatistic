package com.ralabs.gamestatistic.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ralabs.gamestatistic.DemoApplication
import com.ralabs.gamestatistic.config.AmqpTestConfig
import com.ralabs.gamestatistic.domain.GameMessageResponse
import com.ralabs.gamestatistic.listener.TopGamesRequestReceiver
import com.ralabs.gamestatistic.listener.domain.GameChartRequest
import com.ralabs.gamestatistic.repository.GameRepository
import com.ralabs.gamestatistic.service.TestService
import net.minidev.json.parser.JSONParser
import net.minidev.json.parser.ParseException
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.verify
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.time.Duration


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
        classes = [DemoApplication::class, AmqpTestConfig::class],
        properties = ["spring.profiles.active=test"]
)
class RabbitMqControllerTest {

    @Autowired
    lateinit var harness: RabbitListenerTestHarness

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @Autowired
    lateinit var rabbitAdmin: RabbitAdmin

    @Autowired
    lateinit var testService: TestService

    @BeforeAll
    fun sleep() {
        Thread.sleep(5000)
        rabbitAdmin.purgeQueue("all-games-request-queue", true)
        rabbitAdmin.purgeQueue("all-games-response-queue", true)

        rabbitAdmin.purgeQueue("paid-games-request-queue", true)
        rabbitAdmin.purgeQueue("paid-games-response-queue", true)

        rabbitAdmin.purgeQueue("free-games-request-queue", true)
        rabbitAdmin.purgeQueue("free-games-response-queue", true)

        rabbitAdmin.purgeQueue("grossing-games-request-queue", true)
        rabbitAdmin.purgeQueue("grossing-games-response-queue", true)
    }

    @Test
    @DisplayName("should consume message from \"all-games-request-queue\" " +
            "and reply to \"all-games-response-queue\"")
    fun testListenerOnAllGamesRequestQueue() {

        val queue = "all-games-request-queue"
        val id = "all-games-request"
        val request = GameChartRequest(3)
        val message = convertRequestToJson(request)

        val requestListener = getListener(id)

        sendMessageToQueue(queue, message)

        verify(requestListener).handleGamesRequest(message)

        Thread.sleep(5000)


    }

    @Test
    @DisplayName("should consume message from \"paid-games-request-queue\" " +
            "and reply to \"paid-games-response-queue\"")
    fun testListenerOnPaidGamesRequestQueue() {

        val queue = "paid-games-request-queue"
        val id = "paid-games-request"
        val request = "{\"limit\":1}"
        val expectedReply = testService.readGamesFromFile("paid-games-queue-expected-message")


        sendMessageToQueue(queue, request)

        val listener = getListener(id)

        verify(listener).handlePaidGamesRequest(request)

    }

    @Test
    @DisplayName("should consume message from \"free-games-request-queue\" " +
            "and reply to \"free-games-response-queue\"")
    fun testListenerOnFreeGamesRequestQueue() {

        val queue = "free-games-request-queue"
        val id = "free-games-request"
        val message = "{\"limit\":45}"

        val listener = getListener(id)

        sendMessageToQueue(queue, message)

        verify(listener).handleFreeGamesRequest(message)
    }

    @Test
    @DisplayName("should consume message from \"grossing-games-request-queue\" " +
            "and reply to \"grossing-games-response-queue\"")
    fun testListenerOnGrossingGamesRequestQueue() {

        val queue = "grossing-games-request-queue"
        val id = "grossing-games-request"
        val message = "{\"limit\":2}"
        val listener = getListener(id)

        sendMessageToQueue(queue, message)

        verify(listener).handleGrossingGamesRequest(message)
    }

    private fun sendMessageToQueue(queue: String,
                                   message: String): Unit {
        rabbitTemplate.convertAndSend(queue, message)
    }

    private fun getListener(id: String): TopGamesRequestReceiver {
        val listener: TopGamesRequestReceiver = harness.getSpy(id)
        assertNotNull(listener)
        return listener
    }

    private fun convertRequestToJson(reply: GameChartRequest): String = objectMapper.writeValueAsString(reply)


}




