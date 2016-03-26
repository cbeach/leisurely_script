package org.leisurely_script.gdl.expressions

import org.leisurely_script.gdl.BoardRuleSet
import org.leisurely_script.gdl.types.BooleanExpression
import org.leisurely_script.implementation.Board

import scala.util.{Try, Failure, Success}

/**
  * Created by mcsmash on 3/12/16.
  */
case class BoardEmptyExpression(boardRuleSet:BoardRuleSet) extends BooleanExpression {
  override def evaluate:Option[Boolean] = {
    Some(
      boardRuleSet.getPlayableBoard match {
        case Success(board:Board) => {
          board.empty
        }
        case Failure(ex) => throw ex
      }
    )
  }
}

