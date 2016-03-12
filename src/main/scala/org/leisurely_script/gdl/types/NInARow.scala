package org.leisurely_script.gdl.types

import jdk.nashorn.internal.ir.FunctionCall
import org.leisurely_script.gdl.expressions.{Function5CallExpression, Function5CallExpression}
import org.leisurely_script.implementation.Board

import scala.util.{Try, Success, Failure}

import org.leisurely_script.gdl.NeighborType.NeighborType
import org.leisurely_script.gdl.{Player, BoardRuleSet, PieceRule}

/**
  * Created by mcsmash on 3/12/16.
  */
case class NInARowExpression(n:Int, pieceRule:PieceRule, boardRuleSet:BoardRuleSet, player:Player,
                             neighborType:NeighborType=null)
  extends Function5CallExpression(n, pieceRule, boardRuleSet, player, neighborType) {
  def evaluate:BooleanExpression = {
    BooleanExpression(
      boardRuleSet.getPlayableBoard match {
        case Success(board:Board) => board.nInARow(n, pieceRule.getPhysicalPiece(player))
        case Failure(ex) => throw ex
      }
    )
  }
}

