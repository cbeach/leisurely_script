package org.leisurely_script.gdl.expressions

import org.leisurely_script.gdl.types.{BooleanExpression, GameExpression}

/**
  * Created by mcsmash on 3/12/16.
  */
trait FunctionCall[R, +GR <: GameExpression[R]] extends GameExpression[R] {
  override def getChildExpressions:List[GameExpression[Any]]
  override def evaluate:Option[R]
}
trait Function0CallExpression[
R, +GR <: GameExpression[R]] extends FunctionCall[R, GR] {}

trait Function1CallExpression[
R, +GR <: GameExpression[R],
T1, -GT1 <: GameExpression[T1]]
extends FunctionCall[R, GR] {}

trait Function2CallExpression[
R, T1, T2, -GT1 <: GameExpression[T1], -GT2 <: GameExpression[T2], +GR <: GameExpression[R]]
extends FunctionCall[R, GR] {}

trait Method0CallExpression[S, +GS <: GameExpression[S]]
extends FunctionCall[S, GS] {}

trait Method1CallExpression[
S, +GS <: GameExpression[S],
R, +GR <: GameExpression[R],
T1, -GT1 <: GameExpression[T1]]
extends FunctionCall[R, GR] {}

trait Method2CallExpression[
S, +GS <: GameExpression[S],
R, +GR <: GameExpression[R],
T1, -GT1 <: GameExpression[T1],
T2, -GT2 <: GameExpression[T2]]
extends FunctionCall[R, GR] {}
