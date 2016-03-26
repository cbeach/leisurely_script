package org.leisurely_script.test.suites

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite

import org.leisurely_script.gdl._
import org.leisurely_script.gdl.ImplicitDefs.Views.Game._
import org.leisurely_script.repository.LocalStaticRepository
import org.leisurely_script.test.util.GameUtilities.TicTacToeUtilities._

import GameResultState._


class GameUtilityTests extends FunSuite {
  test("The TicTacToeUtilities utility functions should work properly") {
    val tiedGame = movesFromTiedGame(None)
    assert(tiedGame.size == 10)
    assert(tiedGame.last.gameResult.result == Tie)
    assert(boardToString(tiedGame.last.board) == "\n OXX \n XXO \n OOX")

    val fastWinForX = movesFromFastestXWin(None)
    assert(fastWinForX.size == 6)
    assert(fastWinForX.last.gameResult.result == Win)
    assert(boardToString(fastWinForX.last.board) == "\n XXX \n OO- \n ---")

  }
}
