package org.leisurely_script.gdl.types
/**
  * Created by mcsmash on 1/25/16.
  */
case class ShortExpression(value:Short) extends GameAnyVal[Short](value) {
  def !=(other: DoubleExpression): BooleanExpression = BooleanExpression(value != other.value)
  def !=(other: FloatExpression): BooleanExpression = BooleanExpression(value != other.value)
  def !=(other: LongExpression): BooleanExpression = BooleanExpression(value != other.value)
  def !=(other: IntExpression): BooleanExpression = BooleanExpression(value != other.value)
  def !=(other: CharExpression): BooleanExpression = BooleanExpression(value != other.value)
  def !=(other: ShortExpression): BooleanExpression = BooleanExpression(value != other.value)
  def !=(other: ByteExpression): BooleanExpression = BooleanExpression(value != other.value)
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
  def +(other: StringExpression): StringExpression = StringExpression(value + other.value)
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
  def <(other: DoubleExpression): BooleanExpression = BooleanExpression(value < other.value)
  def <(other: FloatExpression): BooleanExpression = BooleanExpression(value < other.value)
  def <(other: LongExpression): BooleanExpression = BooleanExpression(value < other.value)
  def <(other: IntExpression): BooleanExpression = BooleanExpression(value < other.value)
  def <(other: CharExpression): BooleanExpression = BooleanExpression(value < other.value)
  def <(other: ShortExpression): BooleanExpression = BooleanExpression(value < other.value)
  def <(other: ByteExpression): BooleanExpression = BooleanExpression(value < other.value)
  def <<(other: LongExpression): IntExpression = IntExpression(value << other.value)
  def <<(other: IntExpression): IntExpression = IntExpression(value << other.value)
  def <=(other: DoubleExpression): BooleanExpression = BooleanExpression(value <= other.value)
  def <=(other: FloatExpression): BooleanExpression = BooleanExpression(value <= other.value)
  def <=(other: LongExpression): BooleanExpression = BooleanExpression(value <= other.value)
  def <=(other: IntExpression): BooleanExpression = BooleanExpression(value <= other.value)
  def <=(other: CharExpression): BooleanExpression = BooleanExpression(value <= other.value)
  def <=(other: ShortExpression): BooleanExpression = BooleanExpression(value <= other.value)
  def <=(other: ByteExpression): BooleanExpression = BooleanExpression(value <= other.value)
  def ==(other: DoubleExpression): BooleanExpression = BooleanExpression(value == other.value)
  def ==(other: FloatExpression): BooleanExpression = BooleanExpression(value == other.value)
  def ==(other: LongExpression): BooleanExpression = BooleanExpression(value == other.value)
  def ==(other: IntExpression): BooleanExpression = BooleanExpression(value == other.value)
  def ==(other: CharExpression): BooleanExpression = BooleanExpression(value == other.value)
  def ==(other: ShortExpression): BooleanExpression = BooleanExpression(value == other.value)
  def ==(other: ByteExpression): BooleanExpression = BooleanExpression(value == other.value)
  def >(other: DoubleExpression): BooleanExpression = BooleanExpression(value > other.value)
  def >(other: FloatExpression): BooleanExpression = BooleanExpression(value > other.value)
  def >(other: LongExpression): BooleanExpression = BooleanExpression(value > other.value)
  def >(other: IntExpression): BooleanExpression = BooleanExpression(value > other.value)
  def >(other: CharExpression): BooleanExpression = BooleanExpression(value > other.value)
  def >(other: ShortExpression): BooleanExpression = BooleanExpression(value > other.value)
  def >(other: ByteExpression): BooleanExpression = BooleanExpression(value > other.value)
  def >=(other: DoubleExpression): BooleanExpression = BooleanExpression(value >= other.value)
  def >=(other: FloatExpression): BooleanExpression = BooleanExpression(value >= other.value)
  def >=(other: LongExpression): BooleanExpression = BooleanExpression(value >= other.value)
  def >=(other: IntExpression): BooleanExpression = BooleanExpression(value >= other.value)
  def >=(other: CharExpression): BooleanExpression = BooleanExpression(value >= other.value)
  def >=(other: ShortExpression): BooleanExpression = BooleanExpression(value >= other.value)
  def >=(other: ByteExpression): BooleanExpression = BooleanExpression(value >= other.value)
  def >>(other: LongExpression): IntExpression = IntExpression(value >> other.value)
  def >>(other: IntExpression): IntExpression = IntExpression(value >> other.value)
  def >>>(other: LongExpression): IntExpression = IntExpression(value >>> other.value)
  def >>>(other: IntExpression): IntExpression = IntExpression(value >>> other.value)
  def ^(other: LongExpression): LongExpression = LongExpression(value ^ other.value)
  def ^(other: IntExpression): IntExpression = IntExpression(value ^ other.value)
  def ^(other: CharExpression): IntExpression = IntExpression(value ^ other.value)
  def ^(other: ShortExpression): IntExpression = IntExpression(value ^ other.value)
  def ^(other: ByteExpression): IntExpression = IntExpression(value ^ other.value)
  def toByte: ByteExpression = ByteExpression(value.toByte)
  def toChar: CharExpression = CharExpression(value.toChar)
  def toDouble: DoubleExpression = DoubleExpression(value.toDouble)
  def toFloat: FloatExpression = FloatExpression(value.toFloat)
  def toInt: IntExpression = IntExpression(value.toInt)
  def toLong: LongExpression = LongExpression(value.toLong)
  def toShort: ShortExpression = ShortExpression(value.toShort)
  def unary_+ = IntExpression(+value)
  def unary_- = IntExpression(-value)
  def unary_~ = IntExpression(~value)
  def |(other: LongExpression): LongExpression = LongExpression(value | other.value)
  def |(other: IntExpression): IntExpression = IntExpression(value | other.value)
  def |(other: CharExpression): IntExpression = IntExpression(value | other.value)
  def |(other: ShortExpression): IntExpression = IntExpression(value | other.value)
  def |(other: ByteExpression): IntExpression = IntExpression(value | other.value)
  def abs: ShortExpression = ShortExpression(value.abs)
  def compare(other: ShortExpression): IntExpression = IntExpression(value compare other.value)
  def compareTo(other: ShortExpression): IntExpression = IntExpression(value compareTo other.value)
  def isValidByte: BooleanExpression = BooleanExpression(value.isValidByte)
  def isValidChar: BooleanExpression = BooleanExpression(value.isValidChar)
  def isValidInt: BooleanExpression = BooleanExpression(value.isValidInt)
  def isValidShort: BooleanExpression = BooleanExpression(value.isValidShort)
  def isWhole(): BooleanExpression = BooleanExpression(value isWhole)
  def max(other: ShortExpression): ShortExpression = ShortExpression(value max other.value)
  def min(other: ShortExpression): ShortExpression = ShortExpression(value min other.value)
  def signum: IntExpression = IntExpression(value.signum)
}
