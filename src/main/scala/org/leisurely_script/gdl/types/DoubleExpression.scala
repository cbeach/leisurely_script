package org.leisurely_script.gdl.types

import org.leisurely_script.gdl.expressions.operator_ast_nodes.ComparisonOperators.Compare


class DoubleExpression extends AnyValExpression[Double] with Compare[Double] {
	val ord = Ordering[Double]
	def this(value:Double) = {
		this
		this.value = value
	}
	def compare[B](other: GameExpression[B]) =
    new LongExpression(other.evaluate match {
      case Some(o: Long) => if(evaluate.get < o) -1 else if(evaluate.get == o) 0 else 1
      case Some(o: Double) => if(evaluate.get < o) -1 else if(evaluate.get == o) 0 else 1
    })
	def compareTo[B](other:GameExpression[B]) = compare(other)
	override def evaluate:Option[Double] = Some(value)
	def %(other: DoubleExpression): DoubleExpression = DoubleExpression(value % other.value)
	def %(other: LongExpression): DoubleExpression = DoubleExpression(value % other.value)
	def *(other: DoubleExpression): DoubleExpression = DoubleExpression(value * other.value)
	def *(other: LongExpression): DoubleExpression = DoubleExpression(value * other.value)
	def +(other: DoubleExpression): DoubleExpression = DoubleExpression(value + other.value)
	def +(other: LongExpression): DoubleExpression = DoubleExpression(value + other.value)
	def -(other: DoubleExpression): DoubleExpression = DoubleExpression(value - other.value)
	def -(other: LongExpression): DoubleExpression = DoubleExpression(value - other.value)
	def /(other: DoubleExpression): DoubleExpression = DoubleExpression(value / other.value)
	def /(other: LongExpression): DoubleExpression = DoubleExpression(value / other.value)
	def toDouble: DoubleExpression = DoubleExpression(value.toDouble)
	def toLong: LongExpression = LongExpression(value.toLong)
	def unary_+ = DoubleExpression(+value)
	def unary_- = DoubleExpression(-value)
	def abs: DoubleExpression = DoubleExpression(value.abs)
	def ceil: DoubleExpression = DoubleExpression(value.ceil)
	def floor: DoubleExpression = DoubleExpression(value.floor)
	def isInfinite(): BooleanExpression = BooleanExpression(value.isInfinite)
	def isInfinity: BooleanExpression = BooleanExpression(value.isInfinity)
	def isNaN: BooleanExpression = BooleanExpression(value.isNaN)
	def isNegInfinity: BooleanExpression = BooleanExpression(value.isNegInfinity)
	def isPosInfinity: BooleanExpression = BooleanExpression(value.isPosInfinity)
	def isValidByte: BooleanExpression = BooleanExpression(value.isValidByte)
	def isValidChar: BooleanExpression = BooleanExpression(value.isValidChar)
	def isValidInt: BooleanExpression = BooleanExpression(value.isValidInt)
	def isValidShort: BooleanExpression = BooleanExpression(value.isValidShort)
	def isWhole(): BooleanExpression = BooleanExpression(value.isWhole)
	def max(other: DoubleExpression): DoubleExpression = DoubleExpression(value.max(other.value))
	def min(other: DoubleExpression): DoubleExpression = DoubleExpression(value.min(other.value))
	def round: LongExpression = LongExpression(value.round)
	def toDegrees: DoubleExpression = DoubleExpression(value.toDegrees)
	def toRadians: DoubleExpression = DoubleExpression(value.toRadians)
}
object DoubleExpression {
	def apply() = new DoubleExpression()
	def apply(value:Double) = new DoubleExpression(value)
}
