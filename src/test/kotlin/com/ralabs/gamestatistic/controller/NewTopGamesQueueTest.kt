package com.ralabs.gamestatistic.controller

import com.nhaarman.mockito_kotlin.*
import com.ralabs.gamestatistic.DemoApplication
import com.ralabs.gamestatistic.config.AmqpTestConfig
import com.ralabs.gamestatistic.models.Game
import com.ralabs.gamestatistic.models.GameType
import com.ralabs.gamestatistic.service.GameService
import com.ralabs.gamestatistic.service.GameTopInsightsGenerator
import com.ralabs.gamestatistic.service.TestService
import com.ralabs.gamestatistic.service.TopGamesInsightsPublisher
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mock
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import java.util.stream.Collectors


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
        classes = [DemoApplication::class, AmqpTestConfig::class],
        properties = ["spring.profiles.active=test"]
)
@EnableMongoRepositories(
        basePackages = ["com.ralabs.gamestatistic.repository"]
)
class NewTopGamesQueueTest {

    val wayToGames = "main/resources/"
    val wayToChangedGames = "test/resources/changed_data_for_new-top-games-queue/"

    @Autowired
    lateinit var testService: TestService

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @Autowired
    lateinit var rabbitAdmin: RabbitAdmin

    @Value("\${spring.rabbitmq.template.exchange}")
    lateinit var exchange: String

    @Value("\${rabbitmq.queue.new-top-games}")
    lateinit var newTopGamesQueue: String

    @Autowired
    lateinit var gameService: GameService

    @Mock
    lateinit var topGamesInsightsPublisher: TopGamesInsightsPublisher

    @Autowired
    lateinit var topInsightsGenerator: GameTopInsightsGenerator
//
//    @BeforeAll
//    fun sleep() {
//        Thread.sleep(5000)
//        rabbitAdmin.purgeQueue(newTopGamesQueue, true)
//    }

    @Test
    @DisplayName("should verify TopInsightsGenerator (find new games and games with changed rating). " +
            "Working on to of FREE games")
    fun freeGamesInsightsGeneratorTest() {

        val currentGameList = testService
                .readFromFileAndCreateListOfGames(wayToGames + "free-games-chart", GameType.FREE)
        var nextGameList = testService
                .readFromFileAndCreateListOfGames(wayToChangedGames + "changed-free-games",
                        GameType.FREE)

        nextGameList = testService.changeOrderInList(nextGameList);

        val newTopGameInsight = topInsightsGenerator.findNewGamesInTop(currentGameList as MutableList<Game>,
                nextGameList as MutableList<Game>)

        println("newTopGameInsight $newTopGameInsight")
        val changedRatingGameInsight = topInsightsGenerator.findOrderChangesInTop(currentGameList as MutableList<Game>,
                nextGameList as MutableList<Game>)
        println("changedRatingGameInsight $changedRatingGameInsight")

        testService.checkGeneratedInsights(newTopGameInsight, "FREE name", changedRatingGameInsight, 7);
    }

    @Test
    @DisplayName("should verify TopInsightsGenerator (find new games and games with changed rating). " +
            "Working on to of PAID games")
    fun paidGamesInsightsGeneratorTest() {

        val currentGameList = testService
                .readFromFileAndCreateListOfGames(wayToGames + "paid-games-chart", GameType.PAID)
        var nextGameList = testService
                .readFromFileAndCreateListOfGames(wayToChangedGames + "changed-paid-games",
                        GameType.FREE)

        nextGameList = testService.changeOrderInList(nextGameList);

        val newTopGameInsight = topInsightsGenerator.findNewGamesInTop(currentGameList as MutableList<Game>,
                nextGameList as MutableList<Game>)

        println("newTopGameInsight $newTopGameInsight")
        val changedRatingGameInsight = topInsightsGenerator.findOrderChangesInTop(currentGameList as MutableList<Game>,
                nextGameList as MutableList<Game>)
        println("changedRatingGameInsight $changedRatingGameInsight")

        testService.checkGeneratedInsights(newTopGameInsight,
                "PAID game", changedRatingGameInsight, 4);
    }

    @Test
    @DisplayName("should verify TopInsightsGenerator (find new games and games with changed rating). " +
            "Working on to of GROSSING games")
    fun grossingGamesInsightsGeneratorTest() {

        val currentGameList = testService
                .readFromFileAndCreateListOfGames(
                        wayToGames + "grossing-games-chart", GameType.GROSSING)
        var nextGameList = testService
                .readFromFileAndCreateListOfGames(wayToChangedGames + "changed-grossing-games",
                        GameType.FREE)

        nextGameList = testService.changeOrderInList(nextGameList);

        val newTopGameInsight = topInsightsGenerator.findNewGamesInTop(currentGameList as MutableList<Game>,
                nextGameList as MutableList<Game>)

        println("newTopGameInsight $newTopGameInsight")
        val changedRatingGameInsight = topInsightsGenerator.findOrderChangesInTop(currentGameList as MutableList<Game>,
                nextGameList as MutableList<Game>)
        println("changedRatingGameInsight $changedRatingGameInsight")

        testService.checkGeneratedInsights(newTopGameInsight, "GROSSING game",
                changedRatingGameInsight, 11);
    }
}