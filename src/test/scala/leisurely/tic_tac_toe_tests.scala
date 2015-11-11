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
        Game().add(List(piece)).add(List(endConditions)).add(board).add(players)
    }

    test("Create a TicTacToe game object") {
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
        val first = game()
        val second = first.add(List(piece))
        val third = second.add(endConditions)
        val fourth = third.add(board)
        val fifth = fourth.add(players)

    }
}
