package org.leisurely_script.gdl.types

/**
  * Created by mcsmash on 1/25/16.
  */
case class IntExpression(value:Int) extends GameAnyVal[Int](value) {
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
  def compare(other: IntExpression): IntExpression = IntExpression(value.compare(other.value))
  def compareTo(other: IntExpression): IntExpression = IntExpression(value.compareTo(other.value))
  def isValidByteExpression: BooleanExpression = BooleanExpression(value.isValidByte)
  def isValidCharExpression: BooleanExpression = BooleanExpression(value.isValidChar)
  def isValidIntExpression: BooleanExpression = BooleanExpression(value.isValidInt)
  def isValidLongExpression: BooleanExpression = BooleanExpression(value.isValidLong)
  def isValidShortExpression: BooleanExpression = BooleanExpression(value.isValidShort)
  def isWhole(): BooleanExpression = BooleanExpression(value.isWhole)
  def max(other: IntExpression): IntExpression = IntExpression(value.max(other.value))
  def min(other: IntExpression): IntExpression = IntExpression(value.min(other.value))
  def signum: IntExpression = IntExpression(value.signum)
  def toBinaryStringExpression: StringExpression = StringExpression(value.toBinaryString)
  def toHexStringExpression: StringExpression = StringExpression(value.toHexString)
  def toOctalStringExpression: StringExpression = StringExpression(value.toOctalString)
