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
      O, GO] {}
  case class Operator_<[
  S <: Ordered[Any], GS <: GameExpression[S],
  O <: Ordered[Any], GO <: GameExpression[O]]
  (self: GS, other: GO)
    extends BaseOrderingOperator[S, GS, O, GO] {
    val operator = (left: S, right: O) => left < right
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
  trait OrderedExpression extends GameExpression[Ordered[Any]] {
    def <(other: GameExpression[Ordered[Any]]):
    Operator_<[Ordered[Any], GameExpression[Ordered[Any]], Ordered[Any], GameExpression[Ordered[Any]]] = {
      val gS:GameExpression[Ordered[Any]] = this
      Operator_<(gS, other)
    }
    def <=(other: GameExpression[Ordered[Any]]):
    Operator_<=[Ordered[Any], GameExpression[Ordered[Any]], Ordered[Any], GameExpression[Ordered[Any]]] = {
      val gS:GameExpression[Ordered[Any]] = this
      Operator_<=(gS, other)
    }
    def >(other: GameExpression[Ordered[Any]]):
    Operator_>[Ordered[Any], GameExpression[Ordered[Any]], Ordered[Any], GameExpression[Ordered[Any]]] = {
      val gS:GameExpression[Ordered[Any]] = this
      Operator_>(gS, other)
    }
    def >=(other: GameExpression[Ordered[Any]]):
    Operator_>=[Ordered[Any], GameExpression[Ordered[Any]], Ordered[Any], GameExpression[Ordered[Any]]] = {
      val gS:GameExpression[Ordered[Any]] = this
      Operator_>=(gS, other)
    }
  }

}
