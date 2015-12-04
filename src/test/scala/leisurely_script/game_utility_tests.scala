package org.leisurelyscript.test.suites

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite

import org.leisurelyscript.gdl._
import org.leisurelyscript.gdl.ImplicitDefs.Views.Game._
import org.leisurelyscript.repository.LocalStaticRepository
import org.leisurelyscript.test.util.GameUtilities.TicTacToeUtilities._

import GameResultState._


class GameUtilityTests extends FunSuite {
    test("The TicTacToeUtilities utility functions should work properly") {
        val tiedGame = movesFromTiedGame(None)
        assert(tiedGame.size == 10)
        assert(tiedGame.last.gameResult.get.result == Tie)
        assert(boardToString(tiedGame.last.board) == "\n OXX \n XXO \n OOX") 

        val fastWinForX = movesFromFastestXWin(None)
        assert(tiedGame.size == 6)
        assert(tiedGame.last.gameResult.get.result == Win)
        assert(boardToString(tiedGame.last.board) == "\n XXX \n OO- \n ---") 

    }
}
