package org.leisurely_script.gdl.types

import org.leisurely_script.gdl.expressions.operator_ast_nodes.ComparisonOperators.Compare

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
    })
  def compareTo[B](other:GameExpression[B]) = compare(other)
  override def evaluate:Option[Long] = Some(value)
  def %(other: DoubleExpression): DoubleExpression = DoubleExpression(value % other.value)
  def %(other: LongExpression): LongExpression = LongExpression(value % other.value)
  def &(other: LongExpression): LongExpression = LongExpression(value & other.value)
  def *(other: DoubleExpression): DoubleExpression = DoubleExpression(value * other.value)
  def *(other: LongExpression): LongExpression = LongExpression(value * other.value)
  def +(other: DoubleExpression): DoubleExpression = DoubleExpression(value + other.value)
  def +(other: LongExpression): LongExpression = LongExpression(value + other.value)
  def -(other: DoubleExpression): DoubleExpression = DoubleExpression(value - other.value)
  def -(other: LongExpression): LongExpression = LongExpression(value - other.value)
  def /(other: DoubleExpression): DoubleExpression = DoubleExpression(value / other.value)
  def /(other: LongExpression): LongExpression = LongExpression(value / other.value)
  def <<(other: LongExpression): LongExpression = LongExpression(value << other.value)
  def >>(other: LongExpression): LongExpression = LongExpression(value >> other.value)
  def >>>(other: LongExpression): LongExpression = LongExpression(value >>> other.value)
  def ^(other: LongExpression): LongExpression = LongExpression(value ^ other.value)
  def toDouble: DoubleExpression = DoubleExpression(value.toDouble)
  def toLong: LongExpression = LongExpression(value.toLong)
  def unary_+: = LongExpression(+value)
  def unary_-: = LongExpression(-value)
  def unary_~: = LongExpression(~value)
  def |(other: LongExpression): LongExpression = LongExpression(value | other.value)
  def abs: LongExpression = LongExpression(value.abs)
  def isValidByte: BooleanExpression = BooleanExpression(value.isValidByte)
  def isValidChar: BooleanExpression = BooleanExpression(value.isValidChar)
  def isValidInt: BooleanExpression = BooleanExpression(value.isValidInt)
  def isValidLong: BooleanExpression = BooleanExpression(value.isValidLong)
  def isValidShort: BooleanExpression = BooleanExpression(value.isValidShort)
  def isWhole(): BooleanExpression = BooleanExpression(value isWhole)
  def max(other: LongExpression): LongExpression = LongExpression(value max other.value)
  def min(other: LongExpression): LongExpression = LongExpression(value min other.value)
}
object LongExpression {
  def apply() = new LongExpression()
  def apply(value:Long) = new LongExpression(value)
}
