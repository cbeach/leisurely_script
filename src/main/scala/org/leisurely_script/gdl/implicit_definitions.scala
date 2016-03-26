package org.leisurely_script.gdl.ImplicitDefs

import scala.reflect._
import scala.reflect.runtime.universe._
import org.apache.commons.codec.binary.Base64

import org.leisurely_script.gdl.PieceRule
import org.leisurely_script.gdl.{Player => PlayerClass, EndCondition, PlayerListWrapper,
  PieceRuleListWrapper, EndConditionListWrapper}
import org.leisurely_script.gdl.expressions._


package TypeClasses {
import java.io.{ObjectInputStream, ByteArrayInputStream, ObjectOutputStream, ByteArrayOutputStream}
import org.leisurely_script.gdl._
import org.leisurely_script.gdl.types._
import org.leisurely_script.implementation.Game

import scala.collection.mutable

}
package Views {

import org.leisurely_script.gdl.SpecificPlayer
import org.leisurely_script.gdl.expressions.WellFormedConditionalExpressionBuilder
import org.leisurely_script.gdl.types._

  object Game {
    implicit def playerListToPlayerListWrapper(
      players:List[PlayerClass]):PlayerListWrapper = PlayerListWrapper(players)
    implicit def pieceRuleListToPieceRuleListWrapper(
      pieces:List[PieceRule]):PieceRuleListWrapper = PieceRuleListWrapper(pieces)
    implicit def endConditionListToEndConditionListWrapper(
      endConditions:List[EndCondition]):EndConditionListWrapper = EndConditionListWrapper(endConditions)
    implicit def playerToSpecificPlayer(player:PlayerClass):SpecificPlayer = SpecificPlayer(player)
  }
  object Player {
    implicit def playerToPlayerSet(player:PlayerClass):Set[PlayerClass] = Set(player)
  }
  object TypeConversions {
    implicit def BooleanToBooleanExpression(value:Boolean):BooleanExpression = BooleanExpression(value)
    implicit def ByteToByteExpression(value:Byte):ByteExpression = ByteExpression(value)
    implicit def CharToCharExpression(value:Char):CharExpression = CharExpression(value)
    implicit def DoubleToDoubleExpression(value:Double):DoubleExpression = DoubleExpression(value)
    implicit def FloatToFloatExpression(value:Float):FloatExpression = FloatExpression(value)
    implicit def IntToIntExpression(value:Int):IntExpression = IntExpression(value)
    implicit def LongToLongExpression(value:Long):LongExpression = LongExpression(value)
    implicit def ShortToShortExpression(value:Short):ShortExpression = ShortExpression(value)

    implicit def BooleanExpressionToBoolean(expr:BooleanExpression):Boolean = expr.value
    implicit def ByteExpressionToByte(expr:ByteExpression):Byte = expr.value
    implicit def CharExpressionToChar(expr:CharExpression):Char = expr.value
    implicit def DoubleExpressionToDouble(expr:DoubleExpression):Double = expr.value
    implicit def FloatExpressionToFloat(expr:FloatExpression):Float = expr.value
    implicit def IntExpressionToInt(expr:IntExpression):Int = expr.value
    implicit def LongExpressionToLong(expr:LongExpression):Long = expr.value
    implicit def ShortExpressionToShort(expr:ShortExpression):Short = expr.value

    implicit def WellFormedConditionalBuilderToConditional[T]
      (cBuilder:WellFormedConditionalExpressionBuilder[TRUE, TRUE, T]):ConditionalExpression[T] =
      cBuilder.build
    implicit def FinalizedConditionalBuilderToConditional[T]
      (cBuilder:FinalizedConditionalExpressionBuilder[TRUE, TRUE, T]):ConditionalExpression[T] =
      cBuilder.build
  }
}
