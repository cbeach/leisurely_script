package org.leisurely_script.gdl.expressions.operator_ast_nodes

import org.leisurely_script.gdl.types.BooleanExpression
import org.leisurely_script.gdl.expressions.BaseOperators.BaseBinaryOperator
import org.leisurely_script.gdl.types.GameExpression

/**
  * Created by mcsmash on 4/24/16.
  */
private[gdl] object EquivalenceOperators {
  trait BaseEquivalenceOperator[
  S <: Any, GS <: GameExpression[S],
  O <: Any, GO <: GameExpression[O]]
    extends BooleanExpression with BaseBinaryOperator[
      S, GS,
      Boolean, BooleanExpression,
      O, GO] {}
  case class Operator_!=[S <: Any, GS <: GameExpression[S], O <: Any, GO <: GameExpression[O]]
  (self: GS, other: GO)
    extends BaseEquivalenceOperator[S, GS, O, GO] {
    val operator = (left: S, right: O) => left != right
  }
  case class Operator_==[S <: Any, GS <: GameExpression[S], O <: Any, GO <: GameExpression[O]]
  (self: GS, other: GO)
    extends BaseEquivalenceOperator[S, GS, O, GO] {
    val operator = (left: S, right: O) => left == right
  }
}
