package org.leisurely_script.gdl.expressions.operator_ast_nodes

import org.leisurely_script.gdl.types.BooleanExpression
import org.leisurely_script.gdl.expressions.BaseOperators.BaseBinaryOperator
import org.leisurely_script.gdl.types.GameExpression

/**
  * Created by mcsmash on 4/24/16.
  */
private[gdl] object EquivalenceOperators {
  case class Operator_==[L, R](left: L, right: R, n: Boolean) extends BooleanExpression(n) {}
  case class Operator_!=[L, R](left: L, right: R, n: Boolean) extends BooleanExpression(n) {}
}
