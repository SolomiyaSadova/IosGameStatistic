package com.ralabs.gamestatistic.service

import com.ralabs.gamestatistic.models.Game
import com.ralabs.gamestatistic.service.domain.GameGeneralInfo
import com.ralabs.gamestatistic.service.domain.GameTopInsight
import com.ralabs.gamestatistic.service.domain.NewRatingGameInsight
import com.ralabs.gamestatistic.service.domain.NewTopGameInsight
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.util.stream.Collectors
import java.util.stream.Stream

@Service
class GameTopInsightsGenerator {

    fun generateInsights(currentTopGames: MutableList<Game>,
                         nextTopGames: MutableList<Game>): List<GameTopInsight> {
        val newGamesInTop = findNewGamesInTop(currentTopGames, nextTopGames)
       println("newGamesInTop $newGamesInTop")
        val newGamesRating = findOrderChangesInTop(currentTopGames, nextTopGames)
      println("newGamesRating $newGamesRating")
        return Stream.concat(newGamesInTop.stream(), newGamesRating.stream())
                .collect(Collectors.toList())
    }

    fun findNewGamesInTop(currentTopGames: MutableList<Game>,
                          nextTopGames: MutableList<Game>): List<NewTopGameInsight> {
        val valuesToCheck = currentTopGames.stream().map(Game::name).collect(Collectors.toList());
        val newGames = nextTopGames
                .stream()
                .filter { element -> !(valuesToCheck.contains(element.name)) }
                .collect(Collectors.toList())
//
//        println("New Games in charts $newGames")

        val gamesGeneralInfo = newGames
                .map { Game.toGameGeneralInfo(it) }

        return if (newGames.isNotEmpty()) {
            createNewTopGameInsight(gamesGeneralInfo)
        } else {
            emptyList<NewTopGameInsight>()
        }
    }

    fun findOrderChangesInTop(currentTopGames: MutableList<Game>,
                              nextTopGames: MutableList<Game>): List<NewRatingGameInsight> {

        val newRatingGameInsightList = mutableListOf<NewRatingGameInsight>()

        for(storedGame in currentTopGames) {
               val gameWithChangedRating = compareRatingsInGame(nextTopGames, storedGame)
                if (gameWithChangedRating != null) {
                    newRatingGameInsightList.add(NewRatingGameInsight(storedGame.rating,
                            gameWithChangedRating.rating, Game.toGameGeneralInfo(gameWithChangedRating)))
                }
        }

//        println("Rating change ${newRatingGameInsightList}")
        return newRatingGameInsightList
    }

    private fun createNewTopGameInsight(games: List<GameGeneralInfo>): List<NewTopGameInsight> {
        var newTopGameInsight = mutableListOf<NewTopGameInsight>()
        for (game in games) {
            newTopGameInsight.add(NewTopGameInsight(true, game))
        }
        return newTopGameInsight
    }

    private fun compareRatingsInGame(games: List<Game>, storedGame: Game): Game? {
        val gameFromList = games
                .stream()
                .filter { game -> game.name == storedGame.name }
                .findFirst().orElse(null)

        if (gameFromList != null && (gameFromList.rating != storedGame.rating)) {
            return gameFromList
        }
        return null
    }

}