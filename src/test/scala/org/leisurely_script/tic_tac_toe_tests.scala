package org.leisurely_script.test.suites

import org.leisurely_script.implementation.Game

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite
import org.scalatest.TryValues._


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


class TicTacToeTests extends FunSuite {
  test("Create a TicTacToe game object") {
    val first:GameRuleSet = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT) => tTT
      case Failure(ex) => fail
    }
  }

  test("The game should advance to the next player after applyMove is called") {
    val ticTacToe:Game = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT) => tTT.startGame()
      case Failure(ex) => fail
    }
    val legalMoves:List[Move] = ticTacToe.legalMoves(ticTacToe.current.player)
    assert(ticTacToe.current.player == ticTacToe.player(0))
    val playerX = ticTacToe.player(0)
    val playerO = ticTacToe.player(1)
    val pieceRule = ticTacToe.piece(0)
    val xPiece = pieceRule.getPhysicalPiece(playerX)
    val oPiece = pieceRule.getPhysicalPiece(playerO)

    val firstMove = ticTacToe.applyMove(Move(xPiece, ticTacToe.current.player, Push, ticTacToe.board.boardRuleSet.graph.nodesByCoord(Coordinate(1, 1)))).get
    assert(firstMove.current.player == ticTacToe.player(1))
  }

  test("A user should be able to get a valid list of legal moves.") {
    val ticTacToe:Game = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT) => tTT.startGame()
      case Failure(ex) => fail
    }
    val legalMoves:List[Move] = ticTacToe.legalMoves(ticTacToe.current.player)
    assert(legalMoves.size == 9)
    legalMoves.foreach(move => {
      assert(move.player == ticTacToe.current.player)
    })
    assert(legalMoves.map(move => move.player.getPlayers.size == 1 && move.player.getPlayers.head.name == "X").reduce(_&&_))
    val playerX = ticTacToe.player(0)
    val playerO = ticTacToe.player(1)
    val pieceRule = ticTacToe.piece(0)
    val xPiece = pieceRule.getPhysicalPiece(playerX)
    val oPiece = pieceRule.getPhysicalPiece(playerO)
    val firstMove = ticTacToe.applyMove(Move(xPiece, ticTacToe.current.player, Push, ticTacToe.board.boardRuleSet.graph.nodesByCoord(Coordinate(1, 1)))).get
    val legalMovesForTurn2:List[Move] = firstMove.legalMoves(firstMove.current.player)

    assert(legalMovesForTurn2.size == 8)
    legalMovesForTurn2.foreach(move => {
      assert(move.player == firstMove.current.player)
    })
    assert(legalMovesForTurn2.map(move => move.player.getPlayers.size == 1 && move.player.getPlayers.head.name == "O").reduce(_&&_))
  }

  test("A full game of TicTacToe should be possible") {
    val ticTacToe:Game = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT) => tTT.startGame()
      case Failure(ex) => fail
    }
    val playerX = ticTacToe.player(0)
    val playerO = ticTacToe.player(1)
    val pieceRule = ticTacToe.piece(0)
    val xPiece = pieceRule.getPhysicalPiece(playerX)
    val oPiece = pieceRule.getPhysicalPiece(playerO)

    val move1 = ticTacToe.applyMove(Move(xPiece, ticTacToe.current.player, Push, ticTacToe.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 0)))).get
    val move2 = move1.applyMove(Move(oPiece, move1.current.player, Push, move1.board.boardRuleSet.graph.nodesByCoord(Coordinate(1, 0)))).get
    val move3 = move2.applyMove(Move(xPiece, move2.current.player, Push, move2.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 1)))).get
    val move4 = move3.applyMove(Move(oPiece, move3.current.player, Push, move3.board.boardRuleSet.graph.nodesByCoord(Coordinate(1, 1)))).get
    val move5 = move4.applyMove(Move(xPiece, move4.current.player, Push, move4.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 2)))).get

    intercept[IllegalGameStateException] {
      move5.applyMove(Move(oPiece, move5.current.player, Push, move5.board.boardRuleSet.graph.nodesByCoord(Coordinate(1, 2)))).get
    }
  }

  test("No more moves are possible after a player has won.") {
    val ticTacToe:Game = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT) => tTT.startGame()
      case Failure(ex) => fail
    }
    val playerX = ticTacToe.player(0)
    val playerO = ticTacToe.player(1)

    val pieceRule = ticTacToe.piece(0)

    val xPiece = pieceRule.getPhysicalPiece(playerX)
    val oPiece = pieceRule.getPhysicalPiece(playerO)

    val move1 = ticTacToe.applyMove(Move(xPiece, ticTacToe.current.player, Push, ticTacToe.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 0)))).get
    val move2 = move1.applyMove(Move(oPiece, move1.current.player, Push, move1.board.boardRuleSet.graph.nodesByCoord(Coordinate(1, 0)))).get
    val move3 = move2.applyMove(Move(xPiece, move2.current.player, Push, move2.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 1)))).get
    val move4 = move3.applyMove(Move(oPiece, move3.current.player, Push, move3.board.boardRuleSet.graph.nodesByCoord(Coordinate(1, 1)))).get
    val move5 = move4.applyMove(Move(xPiece, move4.current.player, Push, move4.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 2)))).get
    intercept[IllegalGameStateException] {
      val failingMove = move5.applyMove(Move(oPiece, move5.current.player, Push, move5.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 2)))).get
    }
  }

  test("The game history is well formed.") {
    val move0:Game = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT) => tTT.startGame()
      case Failure(ex) => fail
    }
    val pieceRule = move0.piece(0)
    val playerX = move0.player(0)
    val playerO = move0.player(1)
    val xPiece = pieceRule.getPhysicalPiece(playerX)
    val oPiece = pieceRule.getPhysicalPiece(playerO)

    val move1 = move0.applyMove(Move(xPiece, move0.current.player, Push, move0.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 0)))).get
    val move2 = move1.applyMove(Move(oPiece, move1.current.player, Push, move1.board.boardRuleSet.graph.nodesByCoord(Coordinate(2, 0)))).get
    val move3 = move2.applyMove(Move(xPiece, move2.current.player, Push, move2.board.boardRuleSet.graph.nodesByCoord(Coordinate(1, 1)))).get
    val move4 = move3.applyMove(Move(oPiece, move3.current.player, Push, move3.board.boardRuleSet.graph.nodesByCoord(Coordinate(2, 1)))).get
    val move5 = move4.applyMove(Move(xPiece, move4.current.player, Push, move4.board.boardRuleSet.graph.nodesByCoord(Coordinate(2, 2)))).get

    assert(move0.history.size == 0)
    assert(move1.history.size == 1)
    assert(move2.history.size == 2)
    assert(move3.history.size == 3)
    assert(move4.history.size == 4)
    assert(move5.history.size == 5)

    assert(move5.history(0) eq move4)
    assert(move5.history(1) eq move3)
    assert(move5.history(2) eq move2)
    assert(move5.history(3) eq move1)
    assert(move5.history(4) eq move0)
  }
  test("Players can't move out of turn") {
    val ticTacToe:Game = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT) => tTT.startGame()
      case Failure(ex) => fail
    }
    val playerX = ticTacToe.player(0)
    val playerO = ticTacToe.player(1)

    val pieceRule = ticTacToe.piece(0)

    val xPiece = pieceRule.getPhysicalPiece(playerX)
    val oPiece = pieceRule.getPhysicalPiece(playerO)

    val move1 = ticTacToe.applyMove(Move(xPiece, ticTacToe.player(0), Push,
      ticTacToe.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 0)))).get
    intercept[IllegalMoveException] {
      val move2:Game = move1.applyMove(Move(oPiece, ticTacToe.player(0), Push,
        move1.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 1)))).get
    }
  }

  test("nInARow should not return true for tic-tac-toe if the pieces belong to different players.") {
    val ticTacToe = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT) => tTT.startGame()
      case Failure(ex) => fail
    }
    val playerX = ticTacToe.player(0)
    val playerO = ticTacToe.player(1)
    val pieceRule = ticTacToe.piece(0)
    val xPiece = pieceRule.getPhysicalPiece(playerX)
    val oPiece = pieceRule.getPhysicalPiece(playerO)

    val move1 = ticTacToe.applyMove(new Move(xPiece, ticTacToe.current.player, Push, ticTacToe.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 0)))).get
    val move2 = move1.applyMove(new Move(oPiece, move1.current.player, Push, move1.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 1)))).get
    val move3 = move2.applyMove(new Move(xPiece, move2.current.player, Push, move2.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 2)))).get
    assert(move3.status == InProgress)
  }

  test("What about a tied game?") {
    val move0:Game = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT) => tTT.startGame()
      case Failure(ex) => fail
    }
    val playerX = move0.player(0)
    val playerO = move0.player(1)
    val pieceRule = move0.piece(0)
    val xPiece = pieceRule.getPhysicalPiece(playerX)
    val oPiece = pieceRule.getPhysicalPiece(playerO)

    val move1 = move0.applyMove(Move(xPiece, move0.current.player, Push, move0.board.boardRuleSet.graph.nodesByCoord(Coordinate(1, 1)))).get
    val move2 = move1.applyMove(Move(oPiece, move1.current.player, Push, move1.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 0)))).get
    val move3 = move2.applyMove(Move(xPiece, move2.current.player, Push, move2.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 1)))).get
    val move4 = move3.applyMove(Move(oPiece, move3.current.player, Push, move3.board.boardRuleSet.graph.nodesByCoord(Coordinate(2, 1)))).get
    val move5 = move4.applyMove(Move(xPiece, move4.current.player, Push, move4.board.boardRuleSet.graph.nodesByCoord(Coordinate(1, 0)))).get
    val move6 = move5.applyMove(Move(oPiece, move5.current.player, Push, move5.board.boardRuleSet.graph.nodesByCoord(Coordinate(1, 2)))).get
    val move7 = move6.applyMove(Move(xPiece, move6.current.player, Push, move6.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 2)))).get
    val move8 = move7.applyMove(Move(oPiece, move7.current.player, Push, move7.board.boardRuleSet.graph.nodesByCoord(Coordinate(2, 0)))).get
    val move9 = move8.applyMove(Move(xPiece, move8.current.player, Push, move8.board.boardRuleSet.graph.nodesByCoord(Coordinate(2, 2)))).get

    assert(move9.gameResult.result == Tie)
  }
}
