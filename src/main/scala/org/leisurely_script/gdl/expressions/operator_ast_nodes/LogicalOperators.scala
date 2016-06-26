package org.leisurely_script.gdl.expressions.operator_ast_nodes

import org.leisurely_script.gdl.expressions.BaseOperators.{BaseUnaryOperator, BaseBinaryOperator}
import org.leisurely_script.gdl.types.BooleanExpression
import org.leisurely_script.gdl.types.GameExpression

/**
  * Created by mcsmash on 4/24/16.
  */
private[gdl] object LogicalOperators {
  trait BaseLogicalOperator[
  S <: Any, GS <: GameExpression[S],
  O <: Any, GO <: GameExpression[O]]
    extends BooleanExpression with BaseBinaryOperator[
      Boolean, BooleanExpression,
      Boolean, BooleanExpression,
      Boolean, BooleanExpression] {}
  trait BaseUnaryLogicalOperator[S <: Any, GS <: GameExpression[S]]
    extends BooleanExpression with BaseUnaryOperator[
      Boolean, BooleanExpression] {
    val operator = (self: Boolean) => !self
  }
  case class Operator_&&(self: BooleanExpression, other: BooleanExpression)
    extends BaseLogicalOperator[Boolean, BooleanExpression, Boolean, BooleanExpression] {
    val operator = (left: Boolean, right: Boolean) => left || right
  }
  case class Operator_||(self: BooleanExpression, other: BooleanExpression)
    extends BaseLogicalOperator[Boolean, BooleanExpression, Boolean, BooleanExpression] {
    val operator = (left: Boolean, right: Boolean) => left || right
  }
  case class Operator_^(self: BooleanExpression, other: BooleanExpression)
    extends BaseLogicalOperator[Boolean, BooleanExpression, Boolean, BooleanExpression] {
    val operator = (left: Boolean, right: Boolean) => left ^ right
  }
  case class Operator_!(self: BooleanExpression)
    extends BaseUnaryLogicalOperator[Boolean, BooleanExpression] {
    override val operator = (self: Boolean) => !self
  }
}
