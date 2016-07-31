package org.leisurely_script.gdl.expressions.operator_ast_nodes

import org.leisurely_script.gdl.expressions.BaseOperators.BaseBinaryOperator
import org.leisurely_script.gdl.types._

private[gdl] object ComparisonOperators {
  case class Operator_<[L, R](left: L, right: R, n: Boolean) extends BooleanExpression(n) {}
  case class Operator_<=[L, R](left: L, right: R, n: Boolean) extends BooleanExpression(n) {}
  case class Operator_>[L, R](left: L, right: R, n: Boolean) extends BooleanExpression(n) {}
  case class Operator_>=[L, R](left: L, right: R, n: Boolean) extends BooleanExpression(n) {}

  trait Compare[T] {
    outer: GameExpression[T] =>
    val ord: Ordering[T]
    def compare[B](right: GameExpression[B]): LongExpression
    def <[B](other: GameExpression[B]): Operator_<[GameExpression[T], GameExpression[B]] =
      Operator_<[GameExpression[T], GameExpression[B]](outer, other, outer.compare(other).evaluate.get < 0)
    def <=[B](other: GameExpression[B]): Operator_<=[GameExpression[T], GameExpression[B]] =
      Operator_<=[GameExpression[T], GameExpression[B]](outer, other, outer.compare(other).evaluate.get <= 0)
    def >[B](other: GameExpression[B]): Operator_>[GameExpression[T], GameExpression[B]] =
      Operator_>[GameExpression[T], GameExpression[B]](outer, other, outer.compare(other).evaluate.get > 0)
    def >=[B](other: GameExpression[B]): Operator_>=[GameExpression[T], GameExpression[B]] =
      Operator_>=[GameExpression[T], GameExpression[B]](outer, other, outer.compare(other).evaluate.get >= 0)
  }
}
