package org.leisurelyscript.test.suites

import scala.util.{Try, Success, Failure}
import scala.collection.mutable.ListBuffer

import org.scalatest.FunSuite

import org.leisurelyscript.gdl._
import GameStatus._
import org.leisurelyscript.test.util.GameUtilities.TicTacToeUtilities._
import org.leisurelyscript.repository.LocalStaticRepository
import org.leisurelyscript.repository.GameFactory.AvailableGames._


class GamePlayerTests extends FunSuite {
    test("Solve TicTacToe") {
        val numberOfPossibleGames = 255168
        var entryQueue = ListBuffer[Game](LocalStaticRepository.load(TicTacToe).get.startGame())
        var exitQueue = ListBuffer[Game]()
        var counter = 0
        var lastDiff = 0
        var currentDiff = 0
        var currentSum = 0
        while(entryQueue.nonEmpty) {
            currentSum = entryQueue.size + exitQueue.size
            currentDiff = entryQueue.size - exitQueue.size
            println(s"${entryQueue.size} -> ${exitQueue.size} \n\tsum: ${currentSum}\n\tdelta: ${lastDiff - currentDiff}")
            lastDiff = currentDiff
            counter += 1
            val head = entryQueue.head
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
                exitQueue += head
            }

            entryQueue = entryQueue.tail
        }
        assert(exitQueue.size == numberOfPossibleGames)
    }
}
