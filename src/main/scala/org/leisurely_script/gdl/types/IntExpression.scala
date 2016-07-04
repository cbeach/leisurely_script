package org.leisurely_script.gdl.types

import org.leisurely_script.gdl.expressions.operator_ast_nodes.ComparisonOperators.Compare

/**
  * Created by mcsmash on 1/25/16.
  */
class IntExpression extends AnyValExpression[Int] with Compare[Int] {
  val ord = Ordering[Int]
  def this(value:Int) = {
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
  override def evaluate:Option[Int] = Some(value)
  def %(other: DoubleExpression): DoubleExpression = DoubleExpression(value % other.value)
  def %(other: FloatExpression): FloatExpression = FloatExpression(value % other.value)
  def %(other: LongExpression): LongExpression = LongExpression(value % other.value)
  def %(other: IntExpression): IntExpression = IntExpression(value % other.value)
  def %(other: CharExpression): IntExpression = IntExpression(value % other.value)
  def %(other: ShortExpression): IntExpression = IntExpression(value % other.value)
  def %(other: ByteExpression): IntExpression = IntExpression(value % other.value)
  def &(other: LongExpression): LongExpression = LongExpression(value & other.value)
  def &(other: IntExpression): IntExpression = IntExpression(value & other.value)
  def &(other: CharExpression): IntExpression = IntExpression(value & other.value)
  def &(other: ShortExpression): IntExpression = IntExpression(value & other.value)
  def &(other: ByteExpression): IntExpression = IntExpression(value & other.value)
  def *(other: DoubleExpression): DoubleExpression = DoubleExpression(value * other.value)
  def *(other: FloatExpression): FloatExpression = FloatExpression(value * other.value)
  def *(other: LongExpression): LongExpression = LongExpression(value * other.value)
  def *(other: IntExpression): IntExpression = IntExpression(value * other.value)
  def *(other: CharExpression): IntExpression = IntExpression(value * other.value)
  def *(other: ShortExpression): IntExpression = IntExpression(value * other.value)
  def *(other: ByteExpression): IntExpression = IntExpression(value * other.value)
  def +(other: DoubleExpression): DoubleExpression = DoubleExpression(value + other.value)
  def +(other: FloatExpression): FloatExpression = FloatExpression(value + other.value)
  def +(other: LongExpression): LongExpression = LongExpression(value + other.value)
  def +(other: IntExpression): IntExpression = IntExpression(value + other.value)
  def +(other: CharExpression): IntExpression = IntExpression(value + other.value)
  def +(other: ShortExpression): IntExpression = IntExpression(value + other.value)
  def +(other: ByteExpression): IntExpression = IntExpression(value + other.value)
  def -(other: DoubleExpression): DoubleExpression = DoubleExpression(value - other.value)
  def -(other: FloatExpression): FloatExpression = FloatExpression(value - other.value)
  def -(other: LongExpression): LongExpression = LongExpression(value - other.value)
  def -(other: IntExpression): IntExpression = IntExpression(value - other.value)
  def -(other: CharExpression): IntExpression = IntExpression(value - other.value)
  def -(other: ShortExpression): IntExpression = IntExpression(value - other.value)
  def -(other: ByteExpression): IntExpression = IntExpression(value - other.value)
  def /(other: DoubleExpression): DoubleExpression = DoubleExpression(value / other.value)
  def /(other: FloatExpression): FloatExpression = FloatExpression(value / other.value)
  def /(other: LongExpression): LongExpression = LongExpression(value / other.value)
  def /(other: IntExpression): IntExpression = IntExpression(value / other.value)
  def /(other: CharExpression): IntExpression = IntExpression(value / other.value)
  def /(other: ShortExpression): IntExpression = IntExpression(value / other.value)
  def /(other: ByteExpression): IntExpression = IntExpression(value / other.value)
  def <<(other: LongExpression): IntExpression = IntExpression(value << other.value)
  def <<(other: IntExpression): IntExpression = IntExpression(value << other.value)
  def >>(other: LongExpression): IntExpression = IntExpression(value >> other.value)
  def >>(other: IntExpression): IntExpression = IntExpression(value >> other.value)
  def >>>(other: LongExpression): IntExpression = IntExpression(value >>> other.value)
  def >>>(other: IntExpression): IntExpression = IntExpression(value >>> other.value)
  def ^(other: LongExpression): LongExpression = LongExpression(value ^ other.value)
  def ^(other: IntExpression): IntExpression = IntExpression(value ^ other.value)
  def ^(other: CharExpression): IntExpression = IntExpression(value ^ other.value)
  def ^(other: ShortExpression): IntExpression = IntExpression(value ^ other.value)
  def ^(other: ByteExpression): IntExpression = IntExpression(value ^ other.value)
  def toByteExpression: ByteExpression = ByteExpression(value.toByte)
  def toCharExpression: CharExpression = CharExpression(value.toChar)
  def toDoubleExpression: DoubleExpression = DoubleExpression(value.toDouble)
  def toFloatExpression: FloatExpression = FloatExpression(value.toFloat)
  def toIntExpression: IntExpression = IntExpression(value.toInt)
  def toLongExpression: LongExpression = LongExpression(value.toLong)
  def toShortExpression: ShortExpression = ShortExpression(value.toShort)
  def unary_+: = IntExpression(+value)
  def unary_-: = IntExpression(-value)
  def unary_~: = IntExpression(~value)
  def |(other: LongExpression): LongExpression = LongExpression(value | other.value)
  def |(other: IntExpression): IntExpression = IntExpression(value | other.value)
  def |(other: CharExpression): IntExpression = IntExpression(value | other.value)
  def |(other: ShortExpression): IntExpression = IntExpression(value | other.value)
  def |(other: ByteExpression): IntExpression = IntExpression(value | other.value)
  def abs: IntExpression = IntExpression(value.abs)
  def isValidByteExpression: BooleanExpression = BooleanExpression(value.isValidByte)
  def isValidCharExpression: BooleanExpression = BooleanExpression(value.isValidChar)
  def isValidIntExpression: BooleanExpression = BooleanExpression(value.isValidInt)
  def isValidLongExpression: BooleanExpression = BooleanExpression(value.isValidLong)
  def isValidShortExpression: BooleanExpression = BooleanExpression(value.isValidShort)
  def isWhole: BooleanExpression = BooleanExpression(value.isWhole)
  def max(other: IntExpression): IntExpression = IntExpression(value.max(other.value))
  def min(other: IntExpression): IntExpression = IntExpression(value.min(other.value))
  def signum: IntExpression = IntExpression(value.signum)
}
object IntExpression {
  def apply() = new IntExpression()
  def apply(value:Int) = new IntExpression(value)
}
