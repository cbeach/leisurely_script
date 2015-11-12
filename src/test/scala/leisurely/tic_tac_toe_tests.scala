package org.leisurelyscript

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite

import Direction._
import GameStatus._
import GameResultState._
import MoveAction._
import NeighborType._
import Shape._


class TicTacToeTests extends FunSuite {
    def ticTacToeObject:Game = {
        val board = Board(List(3, 3), Square, Indirect, Square) 
        val players = new Players(List(Player("X"), Player("O")))
        val legalMove = new LegalMove(new players.Any(), (game:Game, move:Move) => {
            game.board.graph.nodes(move.node.coord).empty()
        }, Push)
        val piece = new Piece("token", new players.Any(), List[LegalMove](legalMove))
        val endConditions = List(
            EndCondition(Win, new players.Current(), (game:Game) => {
                game.board.nInARow(3, piece).size > 0
            }),
            EndCondition(Tie, new players.All(), (game:Game) => {
                game.board.nInARow(3, piece).size == 0 && game.board.full()
            })
        )
        Game()
            .add(players)
            .add(board)
            .add(List(piece))
            .add(endConditions)
    }

    test("Create a TicTacToe game object") {
        val board = Board(List(3, 3), Square, Indirect, Square) 
        val players = new Players(List(Player("X"), Player("O")))
        val legalMove = new LegalMove(new players.Any(), (game:Game, move:Move) => {
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

    test("A user should be able to get a list of legal moves.") {
        val ticTacToe:Game = ticTacToeObject
        assert(ticTacToe.legalMoves.size == 9)
    }
}
