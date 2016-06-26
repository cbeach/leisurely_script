package org.leisurely_script.gdl.expressions.operator_ast_nodes

import org.leisurely_script.gdl.expressions.BaseOperators.BaseBinaryOperator
import org.leisurely_script.gdl.types.{BooleanExpression, GameExpression}

/**
  * Created by mcsmash on 4/3/16.
  */
private[gdl] object ComparisonOperators {
  trait BaseOrderingOperator[
  S <: Ordered[Any], GS <: GameExpression[S],
  O <: Ordered[Any], GO <: GameExpression[O]]
    extends BooleanExpression with BaseBinaryOperator[
      S, GS,
      Boolean, BooleanExpression,
      O, GO] {
  }
  case class Operator_<[
  S <: Ordered[Any], GS <: GameExpression[S],
  O <: Ordered[Any], GO <: GameExpression[O]]
  (self: GS, other: GO)
    extends BaseOrderingOperator[S, GS, O, GO] {
    val operator = (left: S, right: O) => left < right
  }
}
