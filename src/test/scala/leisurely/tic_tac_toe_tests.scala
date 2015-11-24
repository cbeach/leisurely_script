package org.leisurelyscript

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite
import org.scalatest.TryValues._

import org.leisurelyscript.test.util.TestGameFactory
import org.leisurelyscript.test.util.GameUtilities.TicTacToeUtilities._

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
        val legalMove = new LegalMove(new Current(), (game:Game, move:Move) => {
            game.board.graph.nodes(move.node.coord).empty()
        }, Push)
        val piece = new Piece("token", new Current(), List[LegalMove](legalMove))
        val endConditions = List(
            EndCondition(Win, new Previous(), (game:Game, player:Player) => {
                game.board.nInARow(3, piece).size > 0
            }),
            EndCondition(Tie, new All(), (game:Game, player:Player) => {
                game.board.nInARow(3, piece).size == 0 && game.board.full()
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
        val firstMove = ticTacToe.applyMove(Move(ticTacToe.pieces(0), ticTacToe.players.current, Push, ticTacToe.board.graph.nodes(Coordinate(1, 1)))).get
        assert(firstMove.players.current == ticTacToe.players.all(1)) 
    }

    test("A user should be able to get a valid list of legal moves.") {
        val ticTacToe:Game = TestGameFactory.ticTacToe
        val legalMoves:List[Move] = ticTacToe.legalMoves(ticTacToe.players.current)
        assert(legalMoves.size == 9) 
        legalMoves.foreach(move => {
            assert(move.player == ticTacToe.players.current)
        })
        assert(legalMoves.map(move => move.player.name == "X").reduce(_&&_))
        ticTacToe.startGame()
        val firstMove = ticTacToe.applyMove(Move(ticTacToe.pieces(0), ticTacToe.players.current, Push, ticTacToe.board.graph.nodes(Coordinate(1, 1)))).get
        val moreLegalMoves:List[Move] = firstMove.legalMoves(firstMove.players.current)

        assert(moreLegalMoves.size == 8) 
        moreLegalMoves.foreach(move => {
            assert(move.player == firstMove.players.current)
        })
        assert(moreLegalMoves.map(move => move.player.name == "O").reduce(_&&_))
    }

    test("A full game of TicTacToe should be possible") {
        val ticTacToe:Game = TestGameFactory.ticTacToe.startGame()

        val xPiece = ticTacToe.pieces(0).copy(ticTacToe.players.all(0))
        val oPiece = ticTacToe.pieces(0).copy(ticTacToe.players.all(1))

        val move1 = ticTacToe.applyMove(Move(xPiece, ticTacToe.players.current, Push, ticTacToe.board.graph.nodes(Coordinate(0, 0)))).get
        val move2 = move1.applyMove(Move(oPiece, move1.players.current, Push, move1.board.graph.nodes(Coordinate(1, 0)))).get
        val move3 = move2.applyMove(Move(xPiece, move2.players.current, Push, move2.board.graph.nodes(Coordinate(0, 1)))).get
        val move4 = move3.applyMove(Move(oPiece, move3.players.current, Push, move3.board.graph.nodes(Coordinate(1, 1)))).get
        val move5 = move4.applyMove(Move(xPiece, move4.players.current, Push, move4.board.graph.nodes(Coordinate(0, 2)))).get

        intercept[IllegalGameStateException] {
            move5.applyMove(Move(oPiece, move5.players.current, Push, move5.board.graph.nodes(Coordinate(1, 2)))).get
        }

        val move5Ranking = move5.gameResult.get.ranking.get
        val player = ticTacToe.players.all(0) 
        val piece = ticTacToe.pieces(0).copy(player)

        assert(move5.gameResult.get.result == Win)
        assert(move5Ranking(0)(0) == move5.players.all(0))
    }

    test("No more moves are possible after a player has won.") {
        val ticTacToe:Game = TestGameFactory.ticTacToe.startGame()

        val xPiece = ticTacToe.pieces(0).copy(ticTacToe.players.all(0))
        val oPiece = ticTacToe.pieces(0).copy(ticTacToe.players.all(1))

        val move1 = ticTacToe.applyMove(Move(xPiece, ticTacToe.players.current, Push, ticTacToe.board.graph.nodes(Coordinate(0, 0)))).get
        val move2 = move1.applyMove(Move(oPiece, move1.players.current, Push, move1.board.graph.nodes(Coordinate(1, 0)))).get
        val move3 = move2.applyMove(Move(xPiece, move2.players.current, Push, move2.board.graph.nodes(Coordinate(0, 1)))).get
        val move4 = move3.applyMove(Move(oPiece, move3.players.current, Push, move3.board.graph.nodes(Coordinate(1, 1)))).get
        val move5 = move4.applyMove(Move(xPiece, move4.players.current, Push, move4.board.graph.nodes(Coordinate(0, 2)))).get
        intercept[IllegalGameStateException] {
            val failingMove = move5.applyMove(Move(move5.pieces(0), move5.players.current, Push, move5.board.graph.nodes(Coordinate(0, 2)))).get
        }
    }

    test("The game history is well formed.") {
        val move0:Game = TestGameFactory.ticTacToe
        move0.startGame()
        val move1 = move0.applyMove(Move(move0.pieces(0), move0.players.current, Push, move0.board.graph.nodes(Coordinate(0, 0)))).get
        val move2 = move1.applyMove(Move(move1.pieces(0), move1.players.current, Push, move1.board.graph.nodes(Coordinate(2, 0)))).get
        val move3 = move2.applyMove(Move(move2.pieces(0), move2.players.current, Push, move2.board.graph.nodes(Coordinate(1, 1)))).get
        val move4 = move3.applyMove(Move(move3.pieces(0), move3.players.current, Push, move3.board.graph.nodes(Coordinate(2, 1)))).get
        val move5 = move4.applyMove(Move(move4.pieces(0), move4.players.current, Push, move4.board.graph.nodes(Coordinate(2, 2)))).get

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
        val ticTacToe:Game = TestGameFactory.ticTacToe
        ticTacToe.startGame()
        val move1 = ticTacToe.applyMove(Move(ticTacToe.pieces(0), ticTacToe.players.all(0), Push, 
            ticTacToe.board.graph.nodes(Coordinate(0, 0)))).get
        intercept[IllegalMoveException] {
            val move2:Game = move1.applyMove(Move(move1.pieces(0), ticTacToe.players.all(0), Push, 
                move1.board.graph.nodes(Coordinate(0, 1)))).get
        }
    }

    test("nInARow should not return true for tic-tac-toe if the pieces belong to different players.") {
        val game = TestGameFactory.ticTacToe.startGame()
        val move1 = game.applyMove(new Move(game.pieces(0).copy(game.players.current), 
            game.players.current, Push, game.board.graph.nodes(Coordinate(0, 0)))).get
        val move2 = move1.applyMove(new Move(move1.pieces(0).copy(move1.players.current), 
            move1.players.current, Push, move1.board.graph.nodes(Coordinate(0, 1)))).get
        val endGame = move2.applyMove(new Move(move2.pieces(0).copy(move2.players.current), 
            move2.players.current, Push, move2.board.graph.nodes(Coordinate(0, 2)))).get
        assert(endGame.status == InProgress)
    }

    test("What about a tied game?") {
        val move0:Game = TestGameFactory.ticTacToe.startGame()
        val move1 = move0.applyMove(Move(move0.pieces(0).copy(move0.players.current), move0.players.current, Push, move0.board.graph.nodes(Coordinate(1, 1)))).get
        val move2 = move1.applyMove(Move(move1.pieces(0).copy(move1.players.current), move1.players.current, Push, move1.board.graph.nodes(Coordinate(0, 0)))).get
        val move3 = move2.applyMove(Move(move2.pieces(0).copy(move2.players.current), move2.players.current, Push, move2.board.graph.nodes(Coordinate(0, 1)))).get
        val move4 = move3.applyMove(Move(move3.pieces(0).copy(move3.players.current), move3.players.current, Push, move3.board.graph.nodes(Coordinate(2, 1)))).get
        val move5 = move4.applyMove(Move(move4.pieces(0).copy(move4.players.current), move4.players.current, Push, move4.board.graph.nodes(Coordinate(1, 0)))).get
        val move6 = move5.applyMove(Move(move5.pieces(0).copy(move5.players.current), move5.players.current, Push, move5.board.graph.nodes(Coordinate(1, 2)))).get
        val move7 = move6.applyMove(Move(move6.pieces(0).copy(move6.players.current), move6.players.current, Push, move6.board.graph.nodes(Coordinate(0, 2)))).get
        val move8 = move7.applyMove(Move(move7.pieces(0).copy(move7.players.current), move7.players.current, Push, move7.board.graph.nodes(Coordinate(2, 0)))).get
        val move9 = move8.applyMove(Move(move8.pieces(0).copy(move8.players.current), move8.players.current, Push, move8.board.graph.nodes(Coordinate(2, 2)))).get

        move9.gameResult match {
            case Some(gameResult:GameResult) => {
                assert(gameResult.result == Tie)
            }
            case None => fail
        }
    }
}
