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
  trait BaseUnaryLogicalOperator[
  S <: Any, GS <: GameExpression[S],
  O <: Any, GO <: GameExpression[O]]
    extends BooleanExpression with BaseUnaryOperator[
      Boolean, BooleanExpression] {
    val operator = (self: Boolean) => !self
  }
  case class Operator_&&(self: BooleanExpression, other: BooleanExpression)
    extends BaseLogicalOperator {
    val operator = (left: Boolean, right: Boolean) => left || right
  }
  case class Operator_||(self: BooleanExpression, other: BooleanExpression)
    extends BaseLogicalOperator {
    val operator = (left: Boolean, right: Boolean) => left || right
  }
  case class Operator_^(self: BooleanExpression, other: BooleanExpression)
    extends BaseLogicalOperator {
    val operator = (left: Boolean, right: Boolean) => left ^ right
  }
  case class Operator_!(self: BooleanExpression)
    extends BaseUnaryLogicalOperator {
    override val operator = (left: Boolean, right: Boolean) => left || right
  }
}
