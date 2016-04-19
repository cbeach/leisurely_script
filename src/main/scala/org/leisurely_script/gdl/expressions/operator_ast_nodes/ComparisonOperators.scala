package org.leisurely_script.gdl.expressions.operator_ast_nodes

import org.leisurely_script.gdl.expressions.OperatorASTNodes.BaseOperators.BaseBinaryOperator
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
    def compare(other: GO): Int = {
      self.evaluate match {
        case Some(s: S) => other.evaluate match {
          case Some(o: O) => s.compare(o)
        }
      }
    }
  }
  case class Operator_<=[
  S <: Ordered[Any], GS <: GameExpression[S],
  O <: Ordered[Any], GO <: GameExpression[O]]
  (self: GS, other: GO)
    extends BaseOrderingOperator[S, GS, O, GO] {
    val operator = (left: S, right: O) => left <= right
  }
  case class Operator_>[
  S <: Ordered[Any], GS <: GameExpression[S],
  O <: Ordered[Any], GO <: GameExpression[O]]
  (self: GS, other: GO)
    extends BaseOrderingOperator[S, GS, O, GO] {
    val operator = (left: S, right: O) => left > right
  }
  case class Operator_>=[
  S <: Ordered[Any], GS <: GameExpression[S],
  O <: Ordered[Any], GO <: GameExpression[O]]
  (self: GS, other: GO)
    extends BaseOrderingOperator[S, GS, O, GO] {
    val operator = (left: S, right: O) => left >= right
  }
}
