package org.leisurely_script.gdl.types

import org.leisurely_script.gdl.expressions.OperatorASTNodes.BooleanOperators._
import org.leisurely_script.gdl.expressions.OperatorASTNodes.{Equivalence}

/**
  * Created by mcsmash on 1/25/16.
  */
class BooleanExpression extends AnyValExpression[Boolean] with Equivalence {
  def this(value:Boolean) = {
    this
    this.value = value
  }
  override def evaluate:Option[Boolean] = Some(value)
  def &(other: BooleanExpression):  Operator_& = {
    Operator_&(this, other)
	}
  def &&(other: BooleanExpression):  Operator_&& = {
    Operator_&&(this, other)
	}
  def ^(other: BooleanExpression):  Operator_^ = {
    Operator_^(this, other)
	}
  def unary_!(): Operator_! = {
    Operator_!(this)
	}
  def |(other: BooleanExpression):  Operator_| = {
    Operator_|(this, other)
	}
  def ||(other: BooleanExpression):  Operator_|| = {
    Operator_||(this, other)
	}
  def <(other: BooleanExpression):  Operator_< = {
		Operator_<(this, other)
	}
  def <=(other: BooleanExpression):  Operator_<= = {
		Operator_<=(this, other)
	}
  def >(other: BooleanExpression):  Operator_> = {
		Operator_>(this, other)
	}
  def >=(other: BooleanExpression):  Operator_>= = {
		Operator_>=(this, other)
	}
}
object BooleanExpression {
  def apply() = new BooleanExpression()
  def apply(value:Boolean) = new BooleanExpression(value)
}
