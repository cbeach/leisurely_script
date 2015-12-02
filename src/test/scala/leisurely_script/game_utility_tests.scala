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
        val lastMove = tiedGame(tiedGame.size - 1)
        assert(tiedGame(tiedGame.size - 1).gameResult.get.result == Tie)
        assert(boardToString(lastMove.board) == "\n OXX \n XXO \n OOX") 
    }
}
