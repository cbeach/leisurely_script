package org.leisurely_script.gdl.types

import org.leisurely_script.gdl.expressions.operator_ast_nodes.ComparisonOperators.Compare

/**
  * Created by mcsmash on 1/25/16.
  */
class FloatExpression extends AnyValExpression[Float] with Compare[Float] {
  val ord = Ordering[Float]
  def this(value:Float) = {
    this
    this.value = value
  }
  def compare[B](other: GameExpression[B]) =
    new IntExpression(other.evaluate match {
      case Some(o: Byte) => if(evaluate.get < o.toShort) -1 else if(evaluate.get == o) 0 else 1
      case Some(o: Short) => if(evaluate.get < o) -1 else if(evaluate.get == o) 0 else 1
      case Some(o: Char) => if(evaluate.get < o) -1 else if(evaluate.get == o) 0 else 1
      case Some(o: Int) => if(evaluate.get < o) -1 else if(evaluate.get == o) 0 else 1
      case Some(o: Long) => if(evaluate.get < o) -1 else if(evaluate.get == o) 0 else 1
      case Some(o: Float) => if(evaluate.get < o) -1 else if(evaluate.get == o) 0 else 1
      case Some(o: Double) => if(evaluate.get < o) -1 else if(evaluate.get == o) 0 else 1
    })
  def compareTo[B](other:GameExpression[B]) = compare(other)
  override def evaluate:Option[Float] = Some(value)
  def %(other: DoubleExpression): DoubleExpression = DoubleExpression(value % other.value)
  def %(other: FloatExpression): FloatExpression = FloatExpression(value % other.value)
  def %(other: LongExpression): FloatExpression = FloatExpression(value % other.value)
  def %(other: IntExpression): FloatExpression = FloatExpression(value % other.value)
  def %(other: CharExpression): FloatExpression = FloatExpression(value % other.value)
  def %(other: ShortExpression): FloatExpression = FloatExpression(value % other.value)
  def %(other: ByteExpression): FloatExpression = FloatExpression(value % other.value)
  def *(other: DoubleExpression): DoubleExpression = DoubleExpression(value * other.value)
  def *(other: FloatExpression): FloatExpression = FloatExpression(value * other.value)
  def *(other: LongExpression): FloatExpression = FloatExpression(value * other.value)
  def *(other: IntExpression): FloatExpression = FloatExpression(value * other.value)
  def *(other: CharExpression): FloatExpression = FloatExpression(value * other.value)
  def *(other: ShortExpression): FloatExpression = FloatExpression(value * other.value)
  def *(other: ByteExpression): FloatExpression = FloatExpression(value * other.value)
  def +(other: DoubleExpression): DoubleExpression = DoubleExpression(value + other.value)
  def +(other: FloatExpression): FloatExpression = FloatExpression(value + other.value)
  def +(other: LongExpression): FloatExpression = FloatExpression(value + other.value)
  def +(other: IntExpression): FloatExpression = FloatExpression(value + other.value)
  def +(other: CharExpression): FloatExpression = FloatExpression(value + other.value)
  def +(other: ShortExpression): FloatExpression = FloatExpression(value + other.value)
  def +(other: ByteExpression): FloatExpression = FloatExpression(value + other.value)
  def -(other: DoubleExpression): DoubleExpression = DoubleExpression(value - other.value)
  def -(other: FloatExpression): FloatExpression = FloatExpression(value - other.value)
  def -(other: LongExpression): FloatExpression = FloatExpression(value - other.value)
  def -(other: IntExpression): FloatExpression = FloatExpression(value - other.value)
  def -(other: CharExpression): FloatExpression = FloatExpression(value - other.value)
  def -(other: ShortExpression): FloatExpression = FloatExpression(value - other.value)
  def -(other: ByteExpression): FloatExpression = FloatExpression(value - other.value)
  def /(other: DoubleExpression): DoubleExpression = DoubleExpression(value / other.value)
  def /(other: FloatExpression): FloatExpression = FloatExpression(value / other.value)
  def /(other: LongExpression): FloatExpression = FloatExpression(value / other.value)
  def /(other: IntExpression): FloatExpression = FloatExpression(value / other.value)
  def /(other: CharExpression): FloatExpression = FloatExpression(value / other.value)
  def /(other: ShortExpression): FloatExpression = FloatExpression(value / other.value)
  def /(other: ByteExpression): FloatExpression = FloatExpression(value / other.value)
  def toByte: ByteExpression = ByteExpression(value.toByte)
  def toChar: CharExpression = CharExpression(value.toChar)
  def toDouble: DoubleExpression = DoubleExpression(value.toDouble)
  def toFloat: FloatExpression = FloatExpression(value.toFloat)
  def toInt: IntExpression = IntExpression(value.toInt)
  def toLong: LongExpression = LongExpression(value.toLong)
  def toShort: ShortExpression = ShortExpression(value.toShort)
  def unary_+: = FloatExpression(+value)
  def unary_-: = FloatExpression(-value)
  def abs: FloatExpression = FloatExpression(value.abs)
  def ceil: FloatExpression = FloatExpression(value.ceil)
  def floor: FloatExpression = FloatExpression(value.floor)
  def isInfinity: BooleanExpression = BooleanExpression(value.isInfinity)
  def isNaN: BooleanExpression = BooleanExpression(value.isNaN)
  def isNegInfinity: BooleanExpression = BooleanExpression(value.isNegInfinity)
  def isPosInfinity: BooleanExpression = BooleanExpression(value.isPosInfinity)
  def isValidByte: BooleanExpression = BooleanExpression(value.isValidByte)
  def isValidChar: BooleanExpression = BooleanExpression(value.isValidChar)
  def isValidInt: BooleanExpression = BooleanExpression(value.isValidInt)
  def isValidShort: BooleanExpression = BooleanExpression(value.isValidShort)
  def round: IntExpression = IntExpression(value.round)
  def signum: IntExpression = IntExpression(value.signum)
  def toDegrees: FloatExpression = FloatExpression(value.toDegrees)
  def toRadians: FloatExpression = FloatExpression(value.toRadians)
  def isInfinite(): BooleanExpression = BooleanExpression(value isInfinite)
  def isWhole(): BooleanExpression = BooleanExpression(value isWhole)
  def max(other: FloatExpression): FloatExpression = FloatExpression(value max other.value)
  def min(other: FloatExpression): FloatExpression = FloatExpression(value min other.value)
}
object FloatExpression {
  def apply() = new FloatExpression()
  def apply(value:Float) = new FloatExpression(value)
}
