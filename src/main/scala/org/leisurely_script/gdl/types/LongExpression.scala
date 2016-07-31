package org.leisurely_script.gdl.types

import org.leisurely_script.gdl.expressions.operator_ast_nodes.ComparisonOperators.Compare
import org.leisurely_script.gdl.expressions.operator_ast_nodes.ArithmeticOperators._
import org.leisurely_script.gdl.expressions.operator_ast_nodes.BitwiseOperators._

/**
  * Created by mcsmash on 1/25/16.
  */
class LongExpression extends AnyValExpression[Long] with Compare[Long] {
  val ord = Ordering[Long]
  def this(value:Long) = {
    this
    this.value = value
  }
  def compare[B](other: GameExpression[B]) =
    new LongExpression(other.evaluate match {
      case Some(o: Long) => if(evaluate.get < o) -1 else if(evaluate.get == o) 0 else 1
      case Some(o: Double) => if(evaluate.get < o) -1 else if(evaluate.get == o) 0 else 1
      case _ => throw new IllegalArgumentException("Not a valid number")
    })
  def compareTo[B](other:GameExpression[B]) = compare(other)
  override def evaluate:Option[Long] = Some(value)
  def +(other: DoubleExpression): Operator_+[LongExpression, DoubleExpression] = Operator_+(this, other)
  def +(other: LongExpression): Operator_+[LongExpression, LongExpression] = Operator_+(this, other)
  def -(other: DoubleExpression): Operator_-[LongExpression, DoubleExpression] = Operator_-(this, other)
  def -(other: LongExpression): Operator_-[LongExpression, LongExpression] = Operator_-(this, other)
  def *(other: DoubleExpression): Operator_*[LongExpression, DoubleExpression] = Operator_*(this, other)
  def *(other: LongExpression): Operator_*[LongExpression, LongExpression] = Operator_*(this, other)
  def /(other: DoubleExpression): Operator_/[LongExpression, DoubleExpression] = Operator_/(this, other)
  def /(other: LongExpression): Operator_/[LongExpression, LongExpression] = Operator_/(this, other)
  def %(other: DoubleExpression): Operator_%[LongExpression, DoubleExpression] = Operator_%(this, other)
  def %(other: LongExpression): Operator_%[LongExpression, LongExpression] = Operator_%(this, other)
  def &(other: LongExpression): Operator_& = Operator_&(this, other)
  def |(other: LongExpression): Operator_| = Operator_|(this, other)
  def ^(other: LongExpression): Operator_^ = Operator_^(this, other)
  def <<(other: LongExpression): Operator_<< = Operator_<<(this, other)
  def >>(other: LongExpression): Operator_>> = Operator_>>(this, other)
  def >>>(other: LongExpression): Operator_>>> = Operator_>>>(this, other)
  def toDouble: DoubleExpression = DoubleExpression(value.toDouble)
  def toLong: LongExpression = LongExpression(value)
  def unary_+ = UnaryOperator_+(this)
  def unary_- = UnaryOperator_-(this)
  def unary_~ = UnaryOperator_~(this)
  def abs: LongExpression = LongExpression(value.abs)
  def isWhole(): BooleanExpression = BooleanExpression(value isWhole)
  def max(other: LongExpression): LongExpression = LongExpression(value max other.value)
  def min(other: LongExpression): LongExpression = LongExpression(value min other.value)
}
object LongExpression {
  def apply() = new LongExpression()
  def apply(value:Long) = new LongExpression(value)
}
