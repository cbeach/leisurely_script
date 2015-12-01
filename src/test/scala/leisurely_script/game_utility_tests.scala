package org.leisurelyscript.test.suites

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite
import org.scalatest.TryValues._

import org.leisurelyscript.test.util.TestGameFactory
import org.leisurelyscript.test.util.GameUtilities.TicTacToeUtilities._

import org.leisurelyscript.gdl._
import org.leisurelyscript.gdl.ImplicitDefs.Views.Game._

import Direction._
import GameStatus._
import GameResultState._
import MoveAction._
import NeighborType._
import Shape._


class GameUtilityTests extends FunSuite {
    test("The TicTacToeUtilities utility functions should work properly") {
        val tiedGame = movesFromTiedGame
        val lastMove = tiedGame(tiedGame.size - 1)
        assert(tiedGame(tiedGame.size - 1).gameResult.get.result == Tie)
        assert(boardToString(lastMove.board) == "\n OXX \n XXO \n OOX") 
    }
}
