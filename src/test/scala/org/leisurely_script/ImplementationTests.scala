package org.leisurely_script.test.suites

import org.leisurely_script.implementation.{Board, Game}

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite


import org.leisurely_script.gdl._
import org.leisurely_script.gdl.ImplicitDefs.Views.Game._
import org.leisurely_script.test.util.GameUtilities.TicTacToeUtilities._
import org.leisurely_script.repository.LocalStaticRepository

import Direction._
import GameStatus._
import GameResultState._
import MoveAction._
import NeighborType._
import Shape._


class ImplementationTests extends FunSuite {
  test("Ability to get a playable board from a BoardRuleSet") {
    val ticTacToe: GameRuleSet = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT: GameRuleSet) => tTT
      case Failure(ex) => throw ex
    }
    assert(ticTacToe.board.getPlayableBoard() match {
      case Success(_) => true
      case Failure(_) => false
    })
  }
  test("The playable TicTacToe board has the proper number of occupancy matrices") {
    val ticTacToe: GameRuleSet = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT: GameRuleSet) => tTT
      case Failure(ex) => throw ex
    }
    ticTacToe.board.getPlayableBoard() match {
      case Success(playableBoard: Board) => assert(playableBoard.occupancyMatrices.size == 1)
      case Failure(ex) => throw ex
    }
  }
  test("The playable TicTacToe board's occupancy matrices are the correct size") {
    val ticTacToe: GameRuleSet = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT: GameRuleSet) => tTT
      case Failure(ex) => throw ex
    }
    val token = ticTacToe.pieces(0)
    ticTacToe.board.getPlayableBoard() match {
      case Success(playableBoard: Board) => {
        assert(playableBoard.occupancyMatrices(token).size == 3)
        assert(playableBoard.occupancyMatrices(token)(0).size == 3)
      }
      case Failure(ex) => throw ex
    }
  }
  test("A new Playable board should have empty occupancy matrices") {
    val ticTacToe: GameRuleSet = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT: GameRuleSet) => tTT
      case Failure(ex) => throw ex
    }
    ticTacToe.board.getPlayableBoard() match {
      case Success(playableBoard: Board) => assert(playableBoard.empty)
      case Failure(ex) => throw ex
    }
  }
  test("A playable board's occupancy matrices should be correct after a push") {
    val ticTacToe: GameRuleSet = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT: GameRuleSet) => tTT
      case Failure(ex) => throw ex
    }

    val playerX = ticTacToe.players.all(0)
    val playerO = ticTacToe.players.all(1)

    val pieceRule = ticTacToe.pieces(0)
    val pieceX = pieceRule.getPhysicalPiece(playerX)
    val pieceO = pieceRule.getPhysicalPiece(playerO)

    ticTacToe.board.getPlayableBoard() match {
      case Success(playableBoard: Board) => {
        playableBoard.push(pieceX, Coordinate(0, 0))
        assert(playableBoard.occupancyMatrices(pieceRule)(0)(0) == 1)
        assert(playableBoard.occupancyMatrices(pieceRule)(0)(1) == 0)
        assert(playableBoard.occupancyMatrices(pieceRule)(0)(2) == 0)
        assert(playableBoard.occupancyMatrices(pieceRule)(1)(0) == 0)
        assert(playableBoard.occupancyMatrices(pieceRule)(1)(1) == 0)
        assert(playableBoard.occupancyMatrices(pieceRule)(1)(2) == 0)
        assert(playableBoard.occupancyMatrices(pieceRule)(2)(0) == 0)
        assert(playableBoard.occupancyMatrices(pieceRule)(2)(1) == 0)
        assert(playableBoard.occupancyMatrices(pieceRule)(2)(2) == 0)
        assert(!playableBoard.empty)
      }
      case Failure(ex) => throw ex
    }
  }
  test("A playable board's occupancy matrices should be correct after a push and a pop") {
    val ticTacToe: GameRuleSet = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT: GameRuleSet) => tTT
      case Failure(ex) => throw ex
    }

    val playerX = ticTacToe.players.all(0)
    val playerO = ticTacToe.players.all(1)

    val pieceRule = ticTacToe.pieces(0)
    val pieceX = pieceRule.getPhysicalPiece(playerX)
    val pieceO = pieceRule.getPhysicalPiece(playerO)

    ticTacToe.board.getPlayableBoard() match {
      case Success(playableBoard: Board) => {
        playableBoard.push(pieceX, Coordinate(0, 0))
        playableBoard.pop(Coordinate(0, 0))
        assert(playableBoard.occupancyMatrices(pieceRule)(0)(0) == 0)
        assert(playableBoard.occupancyMatrices(pieceRule)(0)(1) == 0)
        assert(playableBoard.occupancyMatrices(pieceRule)(0)(2) == 0)
        assert(playableBoard.occupancyMatrices(pieceRule)(1)(0) == 0)
        assert(playableBoard.occupancyMatrices(pieceRule)(1)(1) == 0)
        assert(playableBoard.occupancyMatrices(pieceRule)(1)(2) == 0)
        assert(playableBoard.occupancyMatrices(pieceRule)(2)(0) == 0)
        assert(playableBoard.occupancyMatrices(pieceRule)(2)(1) == 0)
        assert(playableBoard.occupancyMatrices(pieceRule)(2)(2) == 0)
        assert(playableBoard.empty)
      }
      case Failure(ex) => throw ex
    }
  }
  test("Graph.setOfNLengthRows should return 8 rows when called on a tic tac toe board.") {
    val ticTacToe: GameRuleSet = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT: GameRuleSet) => tTT
      case Failure(ex) => throw ex
    }
    assert(ticTacToe.board.graph.setOfNLengthRows(3).size == 8)
  }
  test("Board.nInARow works properly.") {
    val ticTacToe: GameRuleSet = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT: GameRuleSet) => tTT
      case Failure(ex) => throw ex
    }

    val playerX = ticTacToe.players.all(0)
    val playerO = ticTacToe.players.all(1)

    val pieceRule = ticTacToe.pieces(0)
    val pieceX = pieceRule.getPhysicalPiece(playerX)
    val pieceO = pieceRule.getPhysicalPiece(playerO)

    ticTacToe.board.getPlayableBoard(nInARow = 3) match {
      case Success(playableBoard: Board) => {
        playableBoard.push(pieceX, Coordinate(0, 0))
        playableBoard.push(pieceX, Coordinate(0, 1))
        playableBoard.push(pieceX, Coordinate(0, 2))
        assert(playableBoard.nInARow(pieceX))
      }
      case Failure(ex) => throw ex
    }
  }
}
