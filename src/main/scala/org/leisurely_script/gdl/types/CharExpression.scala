package org.leisurely_script.gdl.types

import org.leisurely_script.gdl.expressions.operator_ast_nodes.ComparisonOperators.Compare

/**
  * Created by mcsmash on 1/25/16.
  */
class CharExpression extends AnyValExpression[Char] with Compare[Char] {
  val ord = Ordering[Char]
  def this(value:Char) = {
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
  override def evaluate:Option[Char] = Some(value)
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
  def |(other: LongExpression): LongExpression = LongExpression(value | other.value)
  def |(other: IntExpression): IntExpression = IntExpression(value | other.value)
  def |(other: CharExpression): IntExpression = IntExpression(value | other.value)
  def |(other: ShortExpression): IntExpression = IntExpression(value | other.value)
  def |(other: ByteExpression): IntExpression = IntExpression(value | other.value)
  def isWhole(): BooleanExpression = BooleanExpression(value isWhole)
  def max(other: CharExpression): CharExpression = CharExpression(value max other.value)
  def min(other: CharExpression): CharExpression = CharExpression(value min other.value)
  def abs: CharExpression = CharExpression(value.abs)
  def asDigit: IntExpression = IntExpression(value.asDigit)
  def getDirectionality: ByteExpression = ByteExpression(value.getDirectionality)
  def getNumericValue: IntExpression = IntExpression(value.getNumericValue)
  def getType: IntExpression = IntExpression(value.getType)
  def reverseBytes: CharExpression = CharExpression(value.reverseBytes)
  def signum: IntExpression = IntExpression(value.signum)
  def toLower: CharExpression = CharExpression(value.toLower)
  def toTitleCase: CharExpression = CharExpression(value.toTitleCase)
  def toUpper: CharExpression = CharExpression(value.toUpper)
  def isControl: BooleanExpression = BooleanExpression(value.isControl)
  def isDigit: BooleanExpression = BooleanExpression(value.isDigit)
  def isHighSurrogate: BooleanExpression = BooleanExpression(value.isHighSurrogate)
  def isIdentifierIgnorable: BooleanExpression = BooleanExpression(value.isIdentifierIgnorable)
  def isLetter: BooleanExpression = BooleanExpression(value.isLetter)
  def isLetterOrDigit: BooleanExpression = BooleanExpression(value.isLetterOrDigit)
  def isLowSurrogate: BooleanExpression = BooleanExpression(value.isLowSurrogate)
  def isLower: BooleanExpression = BooleanExpression(value.isLower)
  def isMirrored: BooleanExpression = BooleanExpression(value.isMirrored)
  def isSpaceChar: BooleanExpression = BooleanExpression(value.isSpaceChar)
  def isSurrogate: BooleanExpression = BooleanExpression(value.isSurrogate)
  def isTitleCase: BooleanExpression = BooleanExpression(value.isTitleCase)
  def isUnicodeIdentifierPart: BooleanExpression = BooleanExpression(value.isUnicodeIdentifierPart)
  def isUnicodeIdentifierStart: BooleanExpression = BooleanExpression(value.isUnicodeIdentifierStart)
  def isUpper: BooleanExpression = BooleanExpression(value.isUpper)
  def isValidByte: BooleanExpression = BooleanExpression(value.isValidByte)
  def isValidChar: BooleanExpression = BooleanExpression(value.isValidChar)
  def isValidInt: BooleanExpression = BooleanExpression(value.isValidInt)
  def isValidShort: BooleanExpression = BooleanExpression(value.isValidShort)
  def isWhitespace: BooleanExpression = BooleanExpression(value.isWhitespace)
  def toByte: ByteExpression = ByteExpression(value.toByte)
  def toChar: CharExpression = CharExpression(value.toChar)
  def toDouble: DoubleExpression = DoubleExpression(value.toDouble)
  def toFloat: FloatExpression = FloatExpression(value.toFloat)
  def toInt: IntExpression = IntExpression(value.toInt)
  def toLong: LongExpression = LongExpression(value.toLong)
  def toShort: ShortExpression = ShortExpression(value.toShort)
  def unary_+: = IntExpression(+value)
  def unary_-: = IntExpression(-value)
  def unary_~: = IntExpression(~value)
}

object CharExpression {
  def apply() = new CharExpression()
  def apply(value:Char) = new CharExpression(value)
}
