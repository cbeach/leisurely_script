package org.leisurely_script.repository

import org.leisurely_script.gdl.expressions.{NInARowExpression, iff}
import org.leisurely_script.implementation.Game

import scala.util.{Try, Success, Failure}

import org.leisurely_script.gdl._
import org.leisurely_script.gdl.ImplicitDefs.Views.Game._
import org.leisurely_script.gdl.ImplicitDefs.Views.TypeConversions._

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
      EndCondition(iff(board.nInARow(3, piece, PreviousPlayer)) {
        PreviousPlayer.wins
      }),
      EndCondition(iff(!board.nInARow(3, piece, AnyPlayer) && board.full) {
        AllPlayers.ties
      })
    )
    GameRuleSet()
      .add(players)
      .add(board)
      .add(List(piece))
      .add(endConditions)
  }
}
