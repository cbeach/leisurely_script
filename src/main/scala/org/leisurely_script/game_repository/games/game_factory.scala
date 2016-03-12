package org.leisurely_script.repository

import org.leisurely_script.gdl.types.NInARowExpression
import org.leisurely_script.implementation.Game

import scala.util.{Try, Success, Failure}

import org.leisurely_script.gdl._
import org.leisurely_script.gdl.ImplicitDefs.Views.Game._

import GameResultState._
import MoveAction._
import NeighborType._
import Shape._

object GameFactory {
  object AvailableGames extends Enumeration {
    type AvailableGames = Value
    val TicTacToe = Value
  }

  def load(gameID:String):Try[GameRuleSet] = {
    gameID match {
      case "TicTacToe" => Success(ticTacToe)
      case _ => Failure(new GameNotFoundException(s"Can not find the game ${gameID}"))
    }
  }
  def load(gameID:AvailableGames.Value):Try[GameRuleSet] = {
    import AvailableGames._
    gameID match {
      case TicTacToe => Success(ticTacToe)
      case _ => Failure(new GameNotFoundException(s"You shouldn't be here! How did you get here!?"))
    }
  }

  def ticTacToe:GameRuleSet = {
    val players = new Players(List(Player("X"), Player("O")))
    val legalMove = new LegalMove(CurrentPlayer, (game:Game, move:Move) => {
      game.ruleSet.board.graph.nodesByCoord(move.node.coord).empty()
    }, Push)
    val piece = new PieceRule("token", AnyPlayer, List[LegalMove](legalMove))
    val board = BoardRuleSet(List(3, 3), Square, Indirect, Square, List(piece))
    val endConditions = List(
      EndCondition(Win, PreviousPlayer, (game:Game, player:Player) => {
        board.nInARow(3, game.pieces(0), player)
      }),
      EndCondition(Tie, AllPlayers, (game:Game, player:Player) => {
        board.nInARow(3, game.pieces(0), player) && game.board.full
      })
    )
    GameRuleSet()
      .add(players)
      .add(board)
      .add(List(piece))
      .add(endConditions)
  }
}
