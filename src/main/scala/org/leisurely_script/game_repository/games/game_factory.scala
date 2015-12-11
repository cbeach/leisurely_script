package org.leisurelyscript.repository

import scala.util.{Try, Success, Failure}

import org.leisurelyscript.gdl._
import org.leisurelyscript.gdl.ImplicitDefs.Views.Game._

import GameResultState._
import MoveAction._
import NeighborType._
import Shape._

object GameFactory {
    object AvailableGames extends Enumeration {
        type AvailableGames = Value
        val TicTacToe = Value
    }

    def load(gameID:String):Try[Game] = {
        gameID match {
            case "TicTacToe" => Success(ticTacToe)
            case _ => Failure(new GameNotFoundException(s"Can not find the game ${gameID}"))
        }
    }
    def load(gameID:AvailableGames.Value):Try[Game] = {
        import AvailableGames._
        gameID match {
            case TicTacToe => Success(ticTacToe)
            case _ => Failure(new GameNotFoundException(s"You shouldn't be here! How did you get here!?"))
        }
    }

    def ticTacToe:Game = {
        val board = Board(List(3, 3), Square, Indirect, Square) 
        val players = new Players(List(Player("X"), Player("O")))
        val legalMove = new LegalMove(CurrentPlayer, (game:Game, move:Move) => {
            game.board.graph.nodes(move.node.coord).empty()
        }, Push)
        val piece = new PieceRule("token", AnyPlayer, List[LegalMove](legalMove))
        val endConditions = List(
            EndCondition(Win, PreviousPlayer, (game:Game, player:Player) => {
                game.board.nInARow(3, game.pieces(0).getPhysicalPiece(player)).size > 0
            }),
            EndCondition(Tie, AllPlayers, (game:Game, player:Player) => {
                game.board.nInARow(3, game.pieces(0).getPhysicalPiece(player)).size == 0 && game.board.full()
            })
        )
        Game()
            .add(players)
            .add(board)
            .add(List(piece))
            .add(endConditions)
    }
}
