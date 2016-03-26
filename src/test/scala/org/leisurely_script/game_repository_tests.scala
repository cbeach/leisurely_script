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

    val xPiece = tie(0).piece(0).getPhysicalPiece(tie(0).player(0))
    val oPiece = tie(0).piece(0).getPhysicalPiece(tie(0).player(1))

    assert(tie(0).board.nInARow(3, xPiece) == false)
    assert(tie(1).board.nInARow(3, xPiece) == false)
    assert(tie(2).board.nInARow(3, xPiece) == false)
    assert(tie(3).board.nInARow(3, xPiece) == false)
    assert(tie(4).board.nInARow(3, xPiece) == false)
    assert(tie(5).board.nInARow(3, xPiece) == false)
    assert(tie(6).board.nInARow(3, xPiece) == false)
    assert(tie(7).board.nInARow(3, xPiece) == false)
    assert(tie(8).board.nInARow(3, xPiece) == false)
    assert(tie(9).board.nInARow(3, xPiece) == false)

    assert(tie(0).board.nInARow(3, oPiece) == false)
    assert(tie(1).board.nInARow(3, oPiece) == false)
    assert(tie(2).board.nInARow(3, oPiece) == false)
    assert(tie(3).board.nInARow(3, oPiece) == false)
    assert(tie(4).board.nInARow(3, oPiece) == false)
    assert(tie(5).board.nInARow(3, oPiece) == false)
    assert(tie(6).board.nInARow(3, oPiece) == false)
    assert(tie(7).board.nInARow(3, oPiece) == false)
    assert(tie(8).board.nInARow(3, oPiece) == false)
    assert(tie(9).board.nInARow(3, oPiece) == true)

    assert(tie(0).gameResult.result == Pending)
    assert(tie(1).gameResult.result == Pending)
    assert(tie(2).gameResult.result == Pending)
    assert(tie(3).gameResult.result == Pending)
    assert(tie(4).gameResult.result == Pending)
    assert(tie(5).gameResult.result == Pending)
    assert(tie(6).gameResult.result == Pending)
    assert(tie(7).gameResult.result == Pending)
    assert(tie(8).gameResult.result == Pending)
    assert(tie(9).gameResult.result == Tie)

    val playerX = tie(0).all.players(0)
    val playerO = tie(0).all.players(1)

    assert(tie(0).current.player == playerX)
    assert(tie(1).current.player == playerO)
    assert(tie(2).current.player == playerX)
    assert(tie(3).current.player == playerO)
    assert(tie(4).current.player == playerX)
    assert(tie(5).current.player == playerO)
    assert(tie(6).current.player == playerX)
    assert(tie(7).current.player == playerO)
    assert(tie(8).current.player == playerX)
    assert(tie(9).current.player == playerO)

    assert(tie(9).gameResult.result == Tie)
  }
}
