package org.leisurely_script.gdl.expressions.operator_ast_nodes

import org.leisurely_script.gdl.expressions.BaseOperators.BaseBinaryOperator
import org.leisurely_script.gdl.expressions.operator_ast_nodes.EquivalenceOperators.BaseEquivalenceOperator
import org.leisurely_script.gdl.types.{BooleanExpression, GameExpression}

/**
  * Created by mcsmash on 4/24/16.
  */
private[gdl] object BitwiseOperators {
  trait BaseBitWiseOperator[
  S <: AnyVal, GS <: GameExpression[S],
  O <: AnyVal, GO <: GameExpression[O]]
    extends BooleanExpression with BaseBinaryOperator[
      S, GS,
      Boolean, BooleanExpression,
      O, GO] {}
  case class Operator_&[S <: Any, GS <: GameExpression[S], O <: Any, GO <: GameExpression[O]]
  (self: GS, other: GO)(implicit e1: S => Numeric[S])
    extends BaseEquivalenceOperator[S, GS, O, GO] {
    val operator = (left: S, right: O) => left != right
  }
}
