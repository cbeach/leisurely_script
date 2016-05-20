package org.leisurely_script.gdl.expressions

import org.leisurely_script.gdl.types.{AnyValExpression, GameExpression, BooleanExpression}

private[expressions] object BaseOperators {
  trait BaseUnaryOperator
  [S <: AnyVal, GS <: GameExpression[S]]
    extends Method0CallExpression[S, GS] {
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
  S, GS <: GameExpression[S],
  R, GR <: GameExpression[R],
  A, GA <: GameExpression[A]
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
}
