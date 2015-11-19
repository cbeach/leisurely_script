package org.leisurelyscript.test.util

import org.leisurelyscript._

import GameResultState._
import MoveAction._
import NeighborType._
import Shape._

object TestGameFactory {
    def ticTacToe:Game = {
        val board = Board(List(3, 3), Square, Indirect, Square) 
        val players = new Players(List(Player("X"), Player("O")))
        val legalMove = new LegalMove(new players.Current(), (game:Game, move:Move) => {
            game.board.graph.nodes(move.node.coord).empty()
        }, Push)
        val piece = new Piece("token", new players.Any(), List[LegalMove](legalMove))
        val endConditions = List(
            EndCondition(Win, new players.Previous(), (game:Game) => {
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
}
