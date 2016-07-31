package org.leisurely_script.gdl.types


import org.leisurely_script.gdl.expressions.operator_ast_nodes.ComparisonOperators._
import org.leisurely_script.gdl.expressions.operator_ast_nodes.LogicalOperators._

/**
  * Created by mcsmash on 1/25/16.
  */
class BooleanExpression extends AnyValExpression[Boolean] with Compare[Boolean] {
  val ord = Ordering[Boolean]
  def this(value:Boolean) = {
    this
    this.value = value
  }
  override def evaluate:Option[Boolean] = Some(value)
  def compare[B](other: GameExpression[B]): IntExpression =
    new IntExpression(other.evaluate match {
      case Some(o) => o match {
        case o: Boolean => if (evaluate.get < o) -1 else if (evaluate.get == o) 0 else 1
      }
    })

  def &&(other: BooleanExpression):  Operator_&& = {
    Operator_&&(this, other)
  }
  def ||(other: BooleanExpression):  Operator_|| = {
    Operator_||(this, other)
	}
  def ^(other: BooleanExpression):  Operator_^ = {
    Operator_^(this, other)
  }
  def unary_!(): Operator_! = {
    Operator_!(this)
  }
}
object BooleanExpression {
  def apply() = new BooleanExpression()
  def apply(value:Boolean) = new BooleanExpression(value)
}
