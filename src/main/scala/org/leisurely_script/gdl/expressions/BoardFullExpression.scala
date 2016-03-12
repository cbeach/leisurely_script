package org.leisurely_script.gdl.expressions

import org.leisurely_script.gdl.NeighborType.NeighborType
import org.leisurely_script.gdl._
import org.leisurely_script.gdl.types.BooleanExpression
import org.leisurely_script.implementation.Board

import scala.util.{Failure, Success}

/**
  * Created by mcsmash on 3/12/16.
  */
case class BoardFullExpression(boardRuleSet:BoardRuleSet) extends BooleanExpression {
  override def evaluate:BooleanExpression = {
    BooleanExpression(
      boardRuleSet.getPlayableBoard match {
        case Success(board:Board) => {
          board.full
        }
        case Failure(ex) => throw ex
      }
    )
  }
}

