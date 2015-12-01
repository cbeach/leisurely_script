package org.leisurelyscript

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite
import org.scalatest.TryValues._

import org.leisurelyscript.test.util.TestGameFactory
import org.leisurelyscript.test.util.GameUtilities.TicTacToeUtilities._

import org.leisurelyscript.ImplicitDefs.Views.Game._

import Direction._
import GameStatus._
import GameResultState._
import MoveAction._
import NeighborType._
import Shape._


class TicTacToeTests extends FunSuite {
    test("Create a TicTacToe game object") {
        val board = Board(List(3, 3), Square, Indirect, Square) 
        val players = new Players(List(Player("X"), Player("O")))
        val legalMove = new LegalMove(Current, (game:Game, move:Move) => {
            game.board.graph.nodes(move.node.coord).empty()
        }, Push)
        val piece = new PieceRule("token", Current, List[LegalMove](legalMove))
        val endConditions = List(
            EndCondition(Win, Previous, (game:Game, player:Player) => {
                game.board.nInARow(3, piece.getPhysicalPiece(player)).size > 0
            }),
            EndCondition(Tie, All, (game:Game, player:Player) => {
                game.board.nInARow(3, piece.getPhysicalPiece(player)).size == 0 && game.board.full()
            })
        )
        val first = Game()
        val second = first.add(players)
        val third = second.add(board)
        val fourth = third.add(List(piece))
        val fifth = fourth.add(endConditions)
    }

    test("The game should advance to the next player after applyMove is called") {
        val ticTacToe:Game = TestGameFactory.ticTacToe
        val legalMoves:List[Move] = ticTacToe.legalMoves(ticTacToe.players.current)
        assert(ticTacToe.players.current == ticTacToe.players.all(0)) 
        ticTacToe.startGame()
        val playerX = ticTacToe.players.all(0)
        val playerO = ticTacToe.players.all(1)
        val pieceRule = ticTacToe.pieces(0)
        val xPiece = PhysicalPiece(pieceRule.name, playerX)
        val oPiece = PhysicalPiece(pieceRule.name, playerO)

        val firstMove = ticTacToe.applyMove(Move(xPiece, ticTacToe.players.current, Push, ticTacToe.board.graph.nodes(Coordinate(1, 1)))).get
        assert(firstMove.players.current == ticTacToe.players.all(1)) 
    }

    test("A user should be able to get a valid list of legal moves.") {
        val ticTacToe:Game = TestGameFactory.ticTacToe.startGame()
        val legalMoves:List[Move] = ticTacToe.legalMoves(ticTacToe.players.current)
        assert(legalMoves.size == 9) 
        legalMoves.foreach(move => {
            assert(move.player == ticTacToe.players.current)
        })
        assert(legalMoves.map(move => move.player.getPlayers.size == 1 && move.player.getPlayers.head.name == "X").reduce(_&&_))
        val playerX = ticTacToe.players.all(0)
        val playerO = ticTacToe.players.all(1)
        val pieceRule = ticTacToe.pieces(0)
        val xPiece = PhysicalPiece(pieceRule.name, playerX)
        val oPiece = PhysicalPiece(pieceRule.name, playerO)
        val firstMove = ticTacToe.applyMove(Move(xPiece, ticTacToe.players.current, Push, ticTacToe.board.graph.nodes(Coordinate(1, 1)))).get
        val legalMovesForTurn2:List[Move] = firstMove.legalMoves(firstMove.players.current)

        assert(legalMovesForTurn2.size == 8) 
        legalMovesForTurn2.foreach(move => {
            assert(move.player == firstMove.players.current)
        })
        assert(legalMovesForTurn2.map(move => move.player.getPlayers.size == 1 && move.player.getPlayers.head.name == "O").reduce(_&&_))
    }

    test("A full game of TicTacToe should be possible") {
        val ticTacToe:Game = TestGameFactory.ticTacToe.startGame()

        val playerX = ticTacToe.players.all(0)
        val playerO = ticTacToe.players.all(1)
        val pieceRule = ticTacToe.pieces(0)
        val xPiece = PhysicalPiece(pieceRule.name, playerX)
        val oPiece = PhysicalPiece(pieceRule.name, playerO)

        val move1 = ticTacToe.applyMove(Move(xPiece, ticTacToe.players.current, Push, ticTacToe.board.graph.nodes(Coordinate(0, 0)))).get
        val move2 = move1.applyMove(Move(oPiece, move1.players.current, Push, move1.board.graph.nodes(Coordinate(1, 0)))).get
        val move3 = move2.applyMove(Move(xPiece, move2.players.current, Push, move2.board.graph.nodes(Coordinate(0, 1)))).get
        val move4 = move3.applyMove(Move(oPiece, move3.players.current, Push, move3.board.graph.nodes(Coordinate(1, 1)))).get
        val move5 = move4.applyMove(Move(xPiece, move4.players.current, Push, move4.board.graph.nodes(Coordinate(0, 2)))).get

        intercept[IllegalGameStateException] {
            move5.applyMove(Move(oPiece, move5.players.current, Push, move5.board.graph.nodes(Coordinate(1, 2)))).get
        }

        val move5Ranking = move5.gameResult.get.ranking.get

        assert(move5.gameResult.get.result == Win)
        assert(move5Ranking(0)(0) == move5.players.all(0))
    }

    test("No more moves are possible after a player has won.") {
        val ticTacToe:Game = TestGameFactory.ticTacToe.startGame()

        val playerX = ticTacToe.players.all(0)
        val playerO = ticTacToe.players.all(1)

        val pieceRule = ticTacToe.pieces(0)

        val xPiece = PhysicalPiece(pieceRule.name, playerX)
        val oPiece = PhysicalPiece(pieceRule.name, playerO)

        val move1 = ticTacToe.applyMove(Move(xPiece, ticTacToe.players.current, Push, ticTacToe.board.graph.nodes(Coordinate(0, 0)))).get
        val move2 = move1.applyMove(Move(oPiece, move1.players.current, Push, move1.board.graph.nodes(Coordinate(1, 0)))).get
        val move3 = move2.applyMove(Move(xPiece, move2.players.current, Push, move2.board.graph.nodes(Coordinate(0, 1)))).get
        val move4 = move3.applyMove(Move(oPiece, move3.players.current, Push, move3.board.graph.nodes(Coordinate(1, 1)))).get
        val move5 = move4.applyMove(Move(xPiece, move4.players.current, Push, move4.board.graph.nodes(Coordinate(0, 2)))).get
        intercept[IllegalGameStateException] {
            val failingMove = move5.applyMove(Move(oPiece, move5.players.current, Push, move5.board.graph.nodes(Coordinate(0, 2)))).get
        }
    }

    test("The game history is well formed.") {
        val move0:Game = TestGameFactory.ticTacToe.startGame()

        val pieceRule = move0.pieces(0)
        val playerX = move0.players.all(0)
        val playerO = move0.players.all(1)
        val xPiece = PhysicalPiece(pieceRule.name, playerX)
        val oPiece = PhysicalPiece(pieceRule.name, playerO)
        
        val move1 = move0.applyMove(Move(xPiece, move0.players.current, Push, move0.board.graph.nodes(Coordinate(0, 0)))).get
        val move2 = move1.applyMove(Move(oPiece, move1.players.current, Push, move1.board.graph.nodes(Coordinate(2, 0)))).get
        val move3 = move2.applyMove(Move(xPiece, move2.players.current, Push, move2.board.graph.nodes(Coordinate(1, 1)))).get
        val move4 = move3.applyMove(Move(oPiece, move3.players.current, Push, move3.board.graph.nodes(Coordinate(2, 1)))).get
        val move5 = move4.applyMove(Move(xPiece, move4.players.current, Push, move4.board.graph.nodes(Coordinate(2, 2)))).get

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
        val ticTacToe:Game = TestGameFactory.ticTacToe.startGame()
        val playerX = ticTacToe.players.all(0)
        val playerO = ticTacToe.players.all(1)

        val pieceRule = ticTacToe.pieces(0)

        val xPiece = PhysicalPiece(pieceRule.name, playerX)
        val oPiece = PhysicalPiece(pieceRule.name, playerO)

        val move1 = ticTacToe.applyMove(Move(xPiece, ticTacToe.players.all(0), Push, 
            ticTacToe.board.graph.nodes(Coordinate(0, 0)))).get
        intercept[IllegalMoveException] {
            val move2:Game = move1.applyMove(Move(oPiece, ticTacToe.players.all(0), Push, 
                move1.board.graph.nodes(Coordinate(0, 1)))).get
        }
    }

    test("nInARow should not return true for tic-tac-toe if the pieces belong to different players.") {
        val ticTacToe = TestGameFactory.ticTacToe.startGame()

        val playerX = ticTacToe.players.all(0)
        val playerO = ticTacToe.players.all(1)
        val pieceRule = ticTacToe.pieces(0)
        val xPiece = PhysicalPiece(pieceRule.name, playerX)
        val oPiece = PhysicalPiece(pieceRule.name, playerO)

        val move1 = ticTacToe.applyMove(new Move(xPiece, ticTacToe.players.current, Push, ticTacToe.board.graph.nodes(Coordinate(0, 0)))).get
        val move2 = move1.applyMove(new Move(oPiece, move1.players.current, Push, move1.board.graph.nodes(Coordinate(0, 1)))).get
        val move3 = move2.applyMove(new Move(xPiece, move2.players.current, Push, move2.board.graph.nodes(Coordinate(0, 2)))).get
        assert(move3.status == InProgress)
    }

    test("What about a tied game?") {
        val move0:Game = TestGameFactory.ticTacToe.startGame()

        val playerX = move0.players.all(0)
        val playerO = move0.players.all(1)
        val pieceRule = move0.pieces(0)
        val xPiece = PhysicalPiece(pieceRule.name, playerX)
        val oPiece = PhysicalPiece(pieceRule.name, playerO)

        val move1 = move0.applyMove(Move(xPiece, move0.players.current, Push, move0.board.graph.nodes(Coordinate(1, 1)))).get
        val move2 = move1.applyMove(Move(oPiece, move1.players.current, Push, move1.board.graph.nodes(Coordinate(0, 0)))).get
        val move3 = move2.applyMove(Move(xPiece, move2.players.current, Push, move2.board.graph.nodes(Coordinate(0, 1)))).get
        val move4 = move3.applyMove(Move(oPiece, move3.players.current, Push, move3.board.graph.nodes(Coordinate(2, 1)))).get
        val move5 = move4.applyMove(Move(xPiece, move4.players.current, Push, move4.board.graph.nodes(Coordinate(1, 0)))).get
        val move6 = move5.applyMove(Move(oPiece, move5.players.current, Push, move5.board.graph.nodes(Coordinate(1, 2)))).get
        val move7 = move6.applyMove(Move(xPiece, move6.players.current, Push, move6.board.graph.nodes(Coordinate(0, 2)))).get
        val move8 = move7.applyMove(Move(oPiece, move7.players.current, Push, move7.board.graph.nodes(Coordinate(2, 0)))).get
        val move9 = move8.applyMove(Move(xPiece, move8.players.current, Push, move8.board.graph.nodes(Coordinate(2, 2)))).get

        move9.gameResult match {
            case Some(gameResult:GameResult) => {
                assert(gameResult.result == Tie)
            }
            case None => fail
        }
    }
}
