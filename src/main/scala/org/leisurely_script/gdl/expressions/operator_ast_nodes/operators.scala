package org.leisurely_script.gdl.expressions

import scala.math.Numeric

import org.leisurely_script.gdl.expressions.OperatorASTNodes.BaseOperators._
import org.leisurely_script.gdl.types.{AnyValExpression, GameExpression, BooleanExpression}

private[gdl] object OperatorASTNodes {
  private[expressions] object BaseOperators {
    trait BaseUnaryOperator
    [S <: AnyVal, GS <: GameExpression[S]]
      extends Method0CallExpression[S, GS, S, GS] {
      val operator: (S) => S
      val self: GS
      override def evaluate: Option[S] = {
        val left: S = self.evaluate match {
          case Some(value: S) => value
          case None => return None
        }
        Some(operator(left))
      }
    }
    trait BaseBinaryOperator[
    S <: Any, GS <: GameExpression[S],
    R <: Any, GR <: GameExpression[R],
    A <: Any, GA <: GameExpression[A]
    ] extends Method1CallExpression[
      S, GS,
      R, GR,
      A, GA] {
      val operator: (S, A) => R
      val self: GS
      val other: GA

      override def evaluate: Option[R] = {
        val left: S = self.evaluate match {
          case Some(value: S) => value
          case None => return None
        }
        val right: A = other.evaluate match {
          case Some(value: A) => value
          case None => return None
        }
        Some(operator(left, right))
      }
    }
    trait BaseComparisonOperator[
    S <: AnyVal, GS <: GameExpression[S],
    O <: AnyVal, GO <: GameExpression[O]]
      extends BooleanExpression with BaseBinaryOperator[
        S, GS,
        Boolean, BooleanExpression,
        O, GO] {}
    trait BaseOrderingOperator[
    S <: Ordered[O], GS <: GameExpression[S],
    O <: Ordered[S], GO <: GameExpression[O]]
      extends BooleanExpression with BaseBinaryOperator[
        S, GS,
        Boolean, BooleanExpression,
        O, GO] {}
    trait BaseArithmeticOperator[
    S <: AnyVal, GS <: AnyValExpression[S],
    R <: AnyVal, GR <: AnyValExpression[R],
    O <: AnyVal, GO <: AnyValExpression[O]]
      extends AnyValExpression[R] with BaseBinaryOperator[
        S, GS,
        R, GR,
        O, GO] {}
  }
  case class Operator_!=[S <: AnyVal, GS <: GameExpression[S], O <: AnyVal, GO <: GameExpression[O]]
  (self: GS, other: GO)
    extends BaseComparisonOperator[S, GS, O, GO] {
    val operator = (left: S, right: O) => left != right
  }
  case class Operator_==[S <: AnyVal, GS <: GameExpression[S], O <: AnyVal, GO <: GameExpression[O]]
  (self: GS, other: GO)
    extends BaseComparisonOperator[S, GS, O, GO] {
    val operator = (left: S, right: O) => left == right
  }
  case class Operator_<[S <: Ordered[O], GS <: GameExpression[S], O <: Ordered[S], GO <: GameExpression[O]]
  (self: GS, other: GO)
    extends BaseOrderingOperator[S, GS, O, GO] {
    val operator = (left: S, right: O) => left < right
  }
  case class Operator_<=[S <: Ordered[O], GS <: GameExpression[S], O <: Ordered[S], GO <: GameExpression[O]]
  (self: GS, other: GO)
    extends BaseOrderingOperator[S, GS, O, GO] {
    val operator = (left: S, right: O) => left <= right
  }
  case class Operator_>[S <: Ordered[O], GS <: GameExpression[S], O <: Ordered[S], GO <: GameExpression[O]]
  (self: GS, other: GO)
    extends BaseOrderingOperator[S, GS, O, GO] {
    val operator = (left: S, right: O) => left > right
  }
  case class Operator_>=[S <: Ordered[O], GS <: GameExpression[S], O <: Ordered[S], GO <: GameExpression[O]]
  (self: GS, other: GO)
    extends BaseOrderingOperator[S, GS, O, GO] {
    val operator = (left: S, right: O) => left >= right
  }
  object BooleanOperators {
    abstract class BooleanUnaryOperator(self: BooleanExpression)
    extends BooleanExpression with BaseUnaryOperator[Boolean, BooleanExpression] {}
    abstract class BooleanBinaryOperator(self: BooleanExpression, other: BooleanExpression)
    extends BooleanExpression with BaseBinaryOperator[
    Boolean, BooleanExpression,
    Boolean, BooleanExpression,
    Boolean, BooleanExpression] {}

    case class Operator_!=(self: BooleanExpression, other: BooleanExpression) extends BooleanBinaryOperator(self, other) {
      val operator = (left: Boolean, right: Boolean) => left != right
    }

    case class Operator_&(self: BooleanExpression, other: BooleanExpression) extends BooleanBinaryOperator(self, other) {
      val operator = (left: Boolean, right: Boolean) => left & right
    }

    case class Operator_&&(self: BooleanExpression, other: BooleanExpression) extends BooleanBinaryOperator(self, other) {
      val operator = (left: Boolean, right: Boolean) => left && right
    }

    case class Operator_==(self: BooleanExpression, other: BooleanExpression) extends BooleanBinaryOperator(self, other) {
      val operator = (left: Boolean, right: Boolean) => left == right
    }

    case class Operator_^(self: BooleanExpression, other: BooleanExpression) extends BooleanBinaryOperator(self, other) {
      val operator = (left: Boolean, right: Boolean) => left ^ right
    }

    case class Operator_!(self: BooleanExpression) extends BooleanUnaryOperator(self) {
      val operator = (value: Boolean) => !value
    }

    case class Operator_|(self: BooleanExpression, other: BooleanExpression) extends BooleanBinaryOperator(self, other) {
      val operator = (left: Boolean, right: Boolean) => left | right
    }

    case class Operator_||(self: BooleanExpression, other: BooleanExpression) extends BooleanBinaryOperator(self, other) {
      val operator = (left: Boolean, right: Boolean) => left || right
    }

    case class Operator_<(self: BooleanExpression, other: BooleanExpression) extends BooleanBinaryOperator(self, other) {
      val operator = (left: Boolean, right: Boolean) => left < right
    }

    case class Operator_<=(self: BooleanExpression, other: BooleanExpression) extends BooleanBinaryOperator(self, other) {
      val operator = (left: Boolean, right: Boolean) => left <= right
    }

    case class Operator_>(self: BooleanExpression, other: BooleanExpression) extends BooleanBinaryOperator(self, other) {
      val operator = (left: Boolean, right: Boolean) => left > right
    }

    case class Operator_>=(self: BooleanExpression, other: BooleanExpression) extends BooleanBinaryOperator(self, other) {
      val operator = (left: Boolean, right: Boolean) => left >= right
    }
  }
  object ByteOperators {

  }
  object CharOperators {

  }
  object DoubleOperators {

  }
  object FloatOperators {

  }
  object IntOperators {

  }
  object LongOperators {

  }
  object ShortOperators {

  }
}
