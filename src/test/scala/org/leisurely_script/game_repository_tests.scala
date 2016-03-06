package org.leisurely_script.test.suites

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite

import org.leisurely_script.test.util.GameUtilities.TicTacToeUtilities._

import org.leisurely_script.gdl._
import org.leisurely_script.gdl.ImplicitDefs.Views.Game._
import org.leisurely_script.repository.LocalStaticRepository

import Direction._
import GameStatus._
import GameResultState._
import MoveAction._
import NeighborType._
import Shape._


class StaticLocalGameRepoTests extends FunSuite {
  test("The TicTacToe factory should work.") {
    val ticTacToe = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT:GameRuleSet) => tTT
      case Failure(ex) => throw ex
    }

    val tie = movesFromTiedGame(Some(ticTacToe))

    val xPiece = tie(0).pieces(0).getPhysicalPiece(tie(0).players.all(0))
    val oPiece = tie(0).pieces(0).getPhysicalPiece(tie(0).players.all(1))

    assert(tie(0).board.nInARow(xPiece) == false)
    assert(tie(1).board.nInARow(xPiece) == false)
    assert(tie(2).board.nInARow(xPiece) == false)
    assert(tie(3).board.nInARow(xPiece) == false)
    assert(tie(4).board.nInARow(xPiece) == false)
    assert(tie(5).board.nInARow(xPiece) == false)
    assert(tie(6).board.nInARow(xPiece) == false)
    assert(tie(7).board.nInARow(xPiece) == false)
    assert(tie(8).board.nInARow(xPiece) == false)
    assert(tie(9).board.nInARow(xPiece) == false)

    assert(tie(0).board.nInARow(oPiece) == false)
    assert(tie(1).board.nInARow(oPiece) == false)
    assert(tie(2).board.nInARow(oPiece) == false)
    assert(tie(3).board.nInARow(oPiece) == false)
    assert(tie(4).board.nInARow(oPiece) == false)
    assert(tie(5).board.nInARow(oPiece) == false)
    assert(tie(6).board.nInARow(oPiece) == false)
    assert(tie(7).board.nInARow(oPiece) == false)
    assert(tie(8).board.nInARow(oPiece) == false)
    assert(tie(9).board.nInARow(oPiece))

    assert(tie(0).gameResult.get.result == Pending)
    assert(tie(1).gameResult.get.result == Pending)
    assert(tie(2).gameResult.get.result == Pending)
    assert(tie(3).gameResult.get.result == Pending)
    assert(tie(4).gameResult.get.result == Pending)
    assert(tie(5).gameResult.get.result == Pending)
    assert(tie(6).gameResult.get.result == Pending)
    assert(tie(7).gameResult.get.result == Pending)
    assert(tie(8).gameResult.get.result == Pending)
    assert(tie(9).gameResult.get.result == Tie)

    val playerX = tie(0).players.all(0)
    val playerO = tie(0).players.all(1)

    assert(tie(0).players.current == playerX)
    assert(tie(1).players.current == playerO)
    assert(tie(2).players.current == playerX)
    assert(tie(3).players.current == playerO)
    assert(tie(4).players.current == playerX)
    assert(tie(5).players.current == playerO)
    assert(tie(6).players.current == playerX)
    assert(tie(7).players.current == playerO)
    assert(tie(8).players.current == playerX)
    assert(tie(9).players.current == playerO)

    tie(9).gameResult match {
      case Some(gameResult:GameResult) => {
        assert(gameResult.result == Tie)
      }
      case None => fail
    }

  }
}
