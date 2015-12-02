package org.leisurelyscript.test.suites

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite

import org.leisurelyscript.test.util.GameUtilities.TicTacToeUtilities._

import org.leisurelyscript.gdl._
import org.leisurelyscript.gdl.ImplicitDefs.Views.Game._
import org.leisurelyscript.repository.LocalStaticRepository

import Direction._
import GameStatus._
import GameResultState._
import MoveAction._
import NeighborType._
import Shape._


class StaticLocalGameRepoTests extends FunSuite {
    test("The TicTacToe factory should work.") {
        val ticTacToe = LocalStaticRepository.load("TicTacToe") match {
            case Success(tTT:Game) => tTT
            case Failure(ex) => throw ex
        }

        val tie = movesFromTiedGame(Some(ticTacToe))

        val xPiece = tie(0).pieces(0).getPhysicalPiece(tie(0).players.all(0))
        val oPiece = tie(0).pieces(0).getPhysicalPiece(tie(0).players.all(1))

        assert(tie(0).board.nInARow(3, xPiece).size == 0)
        assert(tie(1).board.nInARow(3, xPiece).size == 0)
        assert(tie(2).board.nInARow(3, xPiece).size == 0)
        assert(tie(3).board.nInARow(3, xPiece).size == 0)
        assert(tie(4).board.nInARow(3, xPiece).size == 0)
        assert(tie(5).board.nInARow(3, xPiece).size == 0)
        assert(tie(6).board.nInARow(3, xPiece).size == 0)
        assert(tie(7).board.nInARow(3, xPiece).size == 0)
        assert(tie(8).board.nInARow(3, xPiece).size == 0)
        assert(tie(9).board.nInARow(3, xPiece).size == 0)

        assert(tie(0).board.nInARow(3, oPiece).size == 0)
        assert(tie(1).board.nInARow(3, oPiece).size == 0)
        assert(tie(2).board.nInARow(3, oPiece).size == 0)
        assert(tie(3).board.nInARow(3, oPiece).size == 0)
        assert(tie(4).board.nInARow(3, oPiece).size == 0)
        assert(tie(5).board.nInARow(3, oPiece).size == 0)
        assert(tie(6).board.nInARow(3, oPiece).size == 0)
        assert(tie(7).board.nInARow(3, oPiece).size == 0)
        assert(tie(8).board.nInARow(3, oPiece).size == 0)
        assert(tie(9).board.nInARow(3, oPiece).size == 0)

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
