package org.leisurelyscript

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite
import org.scalatest.TryValues._

import org.leisurelyscript.test.util.TestGameFactory

import Direction._
import GameStatus._
import GameResultState._
import MoveAction._
import NeighborType._
import Shape._


class TicTacToeTests extends FunSuite {
    ignore("Create a TicTacToe game object") {
        val board = Board(List(3, 3), Square, Indirect, Square) 
        val players = new Players(List(Player("X"), Player("O")))
        val legalMove = new LegalMove(new players.Current(), (game:Game, move:Move) => {
            game.board.graph.nodes(move.node.coord).empty()
        }, Push)
        val piece = new Piece("token", new players.Current(), List[LegalMove](legalMove))
        val endConditions = List(
            EndCondition(Win, new players.Previous(), (game:Game) => {
                game.board.nInARow(3, piece).size > 0
            }),
            EndCondition(Tie, new players.All(), (game:Game) => {
                game.board.nInARow(3, piece).size == 0 && game.board.full()
            })
        )
        val first = Game()
        val second = first.add(players)
        val third = second.add(board)
        val fourth = third.add(List(piece))
        val fifth = fourth.add(endConditions)
    }

    ignore("The game should advance to the next player after applyMove is called") {
        val ticTacToe:Game = TestGameFactory.ticTacToe
        val legalMoves:List[Move] = ticTacToe.legalMoves(ticTacToe.players.current)
        assert(ticTacToe.players.current == ticTacToe.players.all(0)) 
        ticTacToe.startGame()
        val firstMove = ticTacToe.applyMove(Move(ticTacToe.pieces(0), ticTacToe.players.current, Push, ticTacToe.board.graph.nodes(Coordinate(1, 1)))).get
        assert(firstMove.players.current == ticTacToe.players.all(1)) 
    }

    ignore("A user should be able to get a valid list of legal moves.") {
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
            //info(s"${move}")
            assert(move.player == firstMove.players.current)
        })
        assert(moreLegalMoves.map(move => move.player.name == "O").reduce(_&&_))
    }

    ignore("A full game of TicTacToe should be possible") {
        val ticTacToe:Game = TestGameFactory.ticTacToe
        ticTacToe.startGame()
        val move1 = ticTacToe.applyMove(Move(ticTacToe.pieces(0), ticTacToe.players.current, Push, ticTacToe.board.graph.nodes(Coordinate(0, 0)))).get
        val move2 = move1.applyMove(Move(move1.pieces(0), move1.players.current, Push, move1.board.graph.nodes(Coordinate(2, 0)))).get
        val move3 = move2.applyMove(Move(move2.pieces(0), move2.players.current, Push, move2.board.graph.nodes(Coordinate(1, 1)))).get
        val move4 = move3.applyMove(Move(move3.pieces(0), move3.players.current, Push, move3.board.graph.nodes(Coordinate(2, 1)))).get
        val move5 = move4.applyMove(Move(move4.pieces(0), move4.players.current, Push, move4.board.graph.nodes(Coordinate(2, 2)))).get
        assert(move5.gameResult.get.result == Win)
        val move5Ranking = move5.gameResult.get.ranking.get
        assert(move5Ranking(0)(0) == move5.players.all(0))
    }

    ignore("No more moves are possible after a player has won.") {
        val ticTacToe:Game = TestGameFactory.ticTacToe
        ticTacToe.startGame()
        val move1 = ticTacToe.applyMove(Move(ticTacToe.pieces(0), ticTacToe.players.current, Push, ticTacToe.board.graph.nodes(Coordinate(0, 0)))).get
        val move2 = move1.applyMove(Move(move1.pieces(0), move1.players.current, Push, move1.board.graph.nodes(Coordinate(2, 0)))).get
        val move3 = move2.applyMove(Move(move2.pieces(0), move2.players.current, Push, move2.board.graph.nodes(Coordinate(1, 1)))).get
        val move4 = move3.applyMove(Move(move3.pieces(0), move3.players.current, Push, move3.board.graph.nodes(Coordinate(2, 1)))).get
        val move5 = move4.applyMove(Move(move4.pieces(0), move4.players.current, Push, move4.board.graph.nodes(Coordinate(2, 2)))).get
        intercept[IllegalGameStateException] {
            val failingMove = move5.applyMove(Move(move5.pieces(0), move5.players.current, Push, move5.board.graph.nodes(Coordinate(0, 2)))).get
        }
    }

    ignore("The game history is well formed.") {
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
            info(s"${move2}")
        }
    }

    test("The game should recognize when an end condition has been met.") {
        val game = TestGameFactory.ticTacToe.startGame()
        val move1 = game.applyMove(new Move(game.pieces(0), game.players.current, Push, 
            game.board.graph.nodes(Coordinate(0, 0)))).get
        val move2 = move1.applyMove(new Move(move1.pieces(0), move1.players.current, Push, 
            move1.board.graph.nodes(Coordinate(0, 1)))).get
        val endGame = move2.applyMove(new Move(move2.pieces(0), move2.players.current, Push, 
            move2.board.graph.nodes(Coordinate(0, 2)))).get
        assert(endGame.status == Finished)
    }

}