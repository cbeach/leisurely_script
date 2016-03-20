package org.leisurely_script.gdl.expressions

import org.leisurely_script.gdl.types._

/**
  * Created by mcsmash on 1/27/16.
  */
class ConditionalExpression[T](val conditionExpr:BooleanExpression,
                               val thenExpr:GameExpression[T],
                               val otherwise:Option[GameExpression[T]] = None) extends GameExpression[T] {
  override def evaluate:Option[T] = {
    if (conditionExpr.evaluate.get) {
      thenExpr.evaluate
    } else {
      otherwise match {
        case Some(expr) => expr.evaluate
        case None => None
      }
    }
  }
}

abstract class TRUE {}
abstract class FALSE {}

private[leisurely_script] class IncompleteConditionalExpressionContainer[HasCond, HasExpr]
    (conditionExpr:BooleanExpression) {
  def apply[T](expr:GameExpression[T]) = {
    new WellFormedConditionalExpressionBuilder[HasCond, TRUE, T] (Some(conditionExpr), Some(expr), None)
  }
}

private[leisurely_script] class WellFormedConditionalExpressionBuilder[HasCond, HasExpr, T]
    (conditionExpr:Option[BooleanExpression], thenExpr:Option[GameExpression[T]],
     otherwiseExpr:Option[GameExpression[T]]) {
  var otherwiseBuilder:Option[WellFormedConditionalExpressionBuilder[TRUE, TRUE, T]] = None
  def otherwise(expr:WellFormedConditionalExpressionBuilder[TRUE, TRUE, T]):WellFormedConditionalExpressionBuilder[HasCond, HasExpr, T] = {
    otherwiseExpr match {
      case Some(_:GameExpression[T]) => throw new RuntimeException("Can not add another otherwise expression")
      case _ => ()
    }
    otherwiseBuilder match {
      case Some(builder:WellFormedConditionalExpressionBuilder[TRUE, TRUE, T]) => builder.otherwise(expr)
      case None => otherwiseBuilder = Some(expr)
    }
    this
  }
  def otherwise(expr:GameExpression[T]) = {
    new FinalizedConditionalExpressionBuilder[HasCond, HasExpr, T] (conditionExpr, thenExpr, Some(expr))
  }
  def build:ConditionalExpression[T] = {
    otherwiseExpr match {
      case Some(expr:GameExpression[T]) =>
        new ConditionalExpression[T](conditionExpr.get, thenExpr.get, otherwiseExpr)
      case None => otherwiseBuilder match {
        case Some(otherwiseIffExpr:WellFormedConditionalExpressionBuilder[TRUE, TRUE, T]) =>
          new ConditionalExpression[T](conditionExpr.get, thenExpr.get, Some(otherwiseIffExpr.build))
        case None =>
          new ConditionalExpression[T](conditionExpr.get, thenExpr.get, None)
      }
    }
  }
}

private[leisurely_script] final class FinalizedConditionalExpressionBuilder[HasCond, HasExpr, T]
    (conditionExpr:Option[BooleanExpression],
     thenExpr:Option[GameExpression[T]],
     otherwiseExpr:Option[GameExpression[T]])
    extends WellFormedConditionalExpressionBuilder[TRUE, TRUE, T](conditionExpr, thenExpr, otherwiseExpr) {
  def this (conditionExpr:Option[BooleanExpression],
            thenExpr:Option[GameExpression[T]],
            otherwiseBuilder:WellFormedConditionalExpressionBuilder[TRUE, TRUE, T]) = {
    this(conditionExpr, thenExpr, None)
    this.otherwiseBuilder = Some(otherwiseBuilder)
  }
}

object iff {
  def apply(condition:BooleanExpression) = {
    new IncompleteConditionalExpressionContainer[TRUE, FALSE](condition)
  }
}
