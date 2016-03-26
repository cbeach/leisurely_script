package org.leisurely_script.test.suites

import org.leisurely_script.implementation.Game

import scala.util.{Try, Success, Failure}
import scala.collection.mutable.Queue

import org.scalatest.FunSuite

import org.leisurely_script.gdl._
import GameStatus._
import org.leisurely_script.test.util.GameUtilities.TicTacToeUtilities._
import org.leisurely_script.test.util.LongRunningTests
import org.leisurely_script.repository.LocalStaticRepository
import org.leisurely_script.repository.GameFactory.AvailableGames._


class GamePlayerTests extends FunSuite {
  test("Solve TicTacToe", LongRunningTests) {
    val numberOfPossibleGames = 255168
    var entryQueue = Queue[Game](LocalStaticRepository.load(TicTacToe).get.startGame())
    var leafNodeCount = 0
    var counter = 0
    while(entryQueue.nonEmpty) {
      val head = entryQueue.dequeue()
      val legalMoves = head.legalMoves(head.current.player)
      if (head.status != Finished) {
        legalMoves.foreach(move => {
          entryQueue += {
            head.applyMove(move) match {
              case Success(nS) => nS
              case Failure(_) => {
                info(s"An illegal move was provided by Game.legalMoves. Move: ${move}\n Current player: ${head.current.player}\n Board: ${boardToString(head.board)}.")
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
