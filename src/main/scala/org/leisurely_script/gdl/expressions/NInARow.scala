package org.leisurely_script.gdl.expressions

import org.leisurely_script.gdl.NeighborType.NeighborType
import org.leisurely_script.gdl._
import org.leisurely_script.gdl.types.BooleanExpression
import org.leisurely_script.implementation.Board

import scala.util.{Failure, Success}

/**
  * Created by mcsmash on 3/12/16.
  */
case class NInARowExpression(n:Int, pieceRule:PieceRule, boardRuleSet:BoardRuleSet, players:PlayerValidator,
                             neighborType:NeighborType=null)
  extends BooleanExpression {
  def gameCreationHook(gameRuleSet:GameRuleSet):Unit = {
    boardRuleSet.addNInARowSet(n, pieceRule, players)
  }
  override def evaluate:Option[Boolean] = {
    Some(boardRuleSet.getPlayableBoard match {
      case Success(board:Board) => {
        val concretePlayers = SomePlayers(players.getPlayers(game.get))
        board.nInARow(n, pieceRule.getPhysicalPiece(concretePlayers))
      }
      case Failure(ex) => throw ex
    })
  }
}

