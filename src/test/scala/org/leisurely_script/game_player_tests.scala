package org.leisurelyscript.test.suites

import scala.util.{Try, Success, Failure}
import scala.collection.mutable.Queue

import org.scalatest.FunSuite

import org.leisurelyscript.gdl._
import GameStatus._
import org.leisurelyscript.test.util.GameUtilities.TicTacToeUtilities._
import org.leisurelyscript.repository.LocalStaticRepository
import org.leisurelyscript.repository.GameFactory.AvailableGames._


class GamePlayerTests extends FunSuite {
    test("Solve TicTacToe") {
        val numberOfPossibleGames = 255168
        var entryQueue = Queue[Game](LocalStaticRepository.load(TicTacToe).get.startGame())
        var leafNodeCount = 0
        var counter = 0
        while(entryQueue.nonEmpty) {
            val head = entryQueue.dequeue()
            val legalMoves = head.legalMoves(head.players.current)
            if (head.status != Finished) {
                legalMoves.foreach(move => {
                    entryQueue += {
                        head.applyMove(move) match {
                            case Success(nS) => nS
                            case Failure(_) => {
                                info(s"An illegal move was provided by Game.legalMoves. Move: ${move}\n Current player: ${head.players.current}\n Board: ${boardToString(head.board)}.")
                                fail
                            }
                        }
                    }
                })
            } else {
                leafNodeCount += 1
            }

            counter += 1
        }
        assert(leafNodeCount == numberOfPossibleGames)
    }
}
