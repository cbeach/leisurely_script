package org.leisurely_script.gdl

import org.leisurely_script.gdl.expressions.OperatorASTNodes.{Operator_==, Equivalence, Operator_!=}

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite

import org.leisurely_script.gdl.types._
import org.leisurely_script.gdl.expressions.iff
import org.leisurely_script.gdl.expressions.OperatorASTNodes.BooleanOperators._
import org.leisurely_script.gdl.GameResultState._
import org.leisurely_script.gdl.ImplicitDefs.Views.TypeConversions._


class DLSTests extends FunSuite {
  test("ByteExpressions evaluate properly.") {
    def eval(b:BooleanExpression): Boolean = {
      b.evaluate.get
    }
    var byteExpr:ByteExpression = ByteExpression(1)

    val doubleExprs = List(DoubleExpression(1), DoubleExpression(2))
    val floatExprs = List(FloatExpression(1), FloatExpression(2))
    val longExprs = List(LongExpression(1), LongExpression(2))
    val intExprs = List(IntExpression(1), IntExpression(2))
    val charExprs = List(CharExpression(1), CharExpression(2))
    val shortExprs = List(ShortExpression(1), ShortExpression(2))
    val byteExprs = List(ByteExpression(1), ByteExpression(2))

    // Comparison Operators
    assert(eval((byteExpr == doubleExprs(0)) && !(byteExpr == doubleExprs(1))))
    assert(eval((byteExpr == floatExprs(0)) && !(byteExpr == floatExprs(1))))
    assert(eval((byteExpr == longExprs(0)) && !(byteExpr == longExprs(1))))
    assert(eval((byteExpr == intExprs(0)) && !(byteExpr == intExprs(1))))
    assert(eval((byteExpr == charExprs(0)) && !(byteExpr == charExprs(1))))
    assert(eval((byteExpr == shortExprs(0)) && !(byteExpr == shortExprs(1))))
    assert(eval((byteExpr == byteExprs(0)) && !(byteExpr == byteExprs(1))))

    assert(eval(!(byteExpr != doubleExprs(0)) && (byteExpr != doubleExprs(1))))
    assert(eval(!(byteExpr != floatExprs(0)) && (byteExpr != floatExprs(1))))
    assert(eval(!(byteExpr != longExprs(0)) && (byteExpr != longExprs(1))))
    assert(eval(!(byteExpr != intExprs(0)) && (byteExpr != intExprs(1))))
    assert(eval(!(byteExpr != charExprs(0)) && (byteExpr != charExprs(1))))
    assert(eval(!(byteExpr != shortExprs(0)) && (byteExpr != shortExprs(1))))
    assert(eval(!(byteExpr != byteExprs(0)) && (byteExpr != byteExprs(1))))

    assert(eval(!(byteExpr > doubleExprs(0)) && !(byteExpr > doubleExprs(1))))
    assert(eval(!(byteExpr > floatExprs(0)) && !(byteExpr > floatExprs(1))))
    assert(eval(!(byteExpr > longExprs(0)) && !(byteExpr > longExprs(1))))
    assert(eval(!(byteExpr > intExprs(0)) && !(byteExpr > intExprs(1))))
    assert(eval(!(byteExpr > charExprs(0)) && !(byteExpr > charExprs(1))))
    assert(eval(!(byteExpr > shortExprs(0)) && !(byteExpr > shortExprs(1))))
    assert(eval(!(byteExpr > byteExprs(0)) && !(byteExpr > byteExprs(1))))

    assert(eval((byteExpr >= doubleExprs(0)) && !(byteExpr >= doubleExprs(1))))
    assert(eval((byteExpr >= floatExprs(0)) && !(byteExpr >= floatExprs(1))))
    assert(eval((byteExpr >= longExprs(0)) && !(byteExpr >= longExprs(1))))
    assert(eval((byteExpr >= intExprs(0)) && !(byteExpr >= intExprs(1))))
    assert(eval((byteExpr >= charExprs(0)) && !(byteExpr >= charExprs(1))))
    assert(eval((byteExpr >= shortExprs(0)) && !(byteExpr >= shortExprs(1))))
    assert(eval((byteExpr >= byteExprs(0)) && !(byteExpr >= byteExprs(1))))

    assert(eval(!(byteExpr < doubleExprs(0)) && (byteExpr < doubleExprs(1))))
    assert(eval(!(byteExpr < floatExprs(0)) && (byteExpr < floatExprs(1))))
    assert(eval(!(byteExpr < longExprs(0)) && (byteExpr < longExprs(1))))
    assert(eval(!(byteExpr < intExprs(0)) && (byteExpr < intExprs(1))))
    assert(eval(!(byteExpr < charExprs(0)) && (byteExpr < charExprs(1))))
    assert(eval(!(byteExpr < shortExprs(0)) && (byteExpr < shortExprs(1))))
    assert(eval(!(byteExpr < byteExprs(0)) && (byteExpr < byteExprs(1))))

    assert(eval((byteExpr <= doubleExprs(0)) && (byteExpr <= doubleExprs(1))))
    assert(eval((byteExpr <= floatExprs(0)) && (byteExpr <= floatExprs(1))))
    assert(eval((byteExpr <= longExprs(0)) && (byteExpr <= longExprs(1))))
    assert(eval((byteExpr <= intExprs(0)) && (byteExpr <= intExprs(1))))
    assert(eval((byteExpr <= charExprs(0)) && (byteExpr <= charExprs(1))))
    assert(eval((byteExpr <= shortExprs(0)) && (byteExpr <= shortExprs(1))))
    assert(eval((byteExpr <= byteExprs(0)) && (byteExpr <= byteExprs(1))))

    assert(+ByteExpression(1) == ByteExpression(1))
    assert(-ByteExpression(1) == ByteExpression(-1))
    assert(~ByteExpression(1) == IntExpression(~1))

    assert(eval((byteExpr + doubleExprs(0)) == ByteExpression(2)))
    assert(eval((byteExpr + floatExprs(0)) == ByteExpression(2)))
    assert(eval((byteExpr + longExprs(0)) == ByteExpression(2)))
    assert(eval((byteExpr + intExprs(0)) == ByteExpression(2)))
    assert(eval((byteExpr + charExprs(0)) == ByteExpression(2)))
    assert(eval((byteExpr + shortExprs(0)) == ByteExpression(2)))
    assert(eval((byteExpr + byteExprs(0)) == ByteExpression(2)))
    assert(eval((byteExpr + doubleExprs(1)) == ByteExpression(3)))
    assert(eval((byteExpr + floatExprs(1)) == ByteExpression(3)))
    assert(eval((byteExpr + longExprs(1)) == ByteExpression(3)))
    assert(eval((byteExpr + intExprs(1)) == ByteExpression(3)))
    assert(eval((byteExpr + charExprs(1)) == ByteExpression(3)))
    assert(eval((byteExpr + shortExprs(1)) == ByteExpression(3)))
    assert(eval((byteExpr + byteExprs(1)) == ByteExpression(3)))

    assert(eval((byteExpr - doubleExprs(0)) == ByteExpression(0)))
    assert(eval((byteExpr - floatExprs(0)) == ByteExpression(0)))
    assert(eval((byteExpr - longExprs(0)) == ByteExpression(0)))
    assert(eval((byteExpr - intExprs(0)) == ByteExpression(0)))
    assert(eval((byteExpr - charExprs(0)) == ByteExpression(0)))
    assert(eval((byteExpr - shortExprs(0)) == ByteExpression(0)))
    assert(eval((byteExpr - byteExprs(0)) == ByteExpression(0)))
    assert(eval((byteExpr - doubleExprs(1)) == IntExpression(-1)))
    assert(eval((byteExpr - floatExprs(1)) == IntExpression(-1)))
    assert(eval((byteExpr - longExprs(1)) == IntExpression(-1)))
    assert(eval((byteExpr - intExprs(1)) == IntExpression(-1)))
    assert(eval((byteExpr - charExprs(1)) == IntExpression(-1)))
    assert(eval((byteExpr - shortExprs(1)) == IntExpression(-1)))
    assert(eval((byteExpr - byteExprs(1)) == IntExpression(-1)))

    assert(eval((byteExpr * doubleExprs(0)) == ByteExpression(1)))
    assert(eval((byteExpr * floatExprs(0)) == ByteExpression(1)))
    assert(eval((byteExpr * longExprs(0)) == ByteExpression(1)))
    assert(eval((byteExpr * intExprs(0)) == ByteExpression(1)))
    assert(eval((byteExpr * charExprs(0)) == ByteExpression(1)))
    assert(eval((byteExpr * shortExprs(0)) == ByteExpression(1)))
    assert(eval((byteExpr * byteExprs(0)) == ByteExpression(1)))
    assert(eval((byteExpr * doubleExprs(1)) == ByteExpression(2)))
    assert(eval((byteExpr * floatExprs(1)) == ByteExpression(2)))
    assert(eval((byteExpr * longExprs(1)) == ByteExpression(2)))
    assert(eval((byteExpr * intExprs(1)) == ByteExpression(2)))
    assert(eval((byteExpr * charExprs(1)) == ByteExpression(2)))
    assert(eval((byteExpr * shortExprs(1)) == ByteExpression(2)))
    assert(eval((byteExpr * byteExprs(1)) == ByteExpression(2)))

    assert(eval((byteExpr / doubleExprs(0)) == ByteExpression(1)))
    assert(eval((byteExpr / floatExprs(0)) == ByteExpression(1)))
    assert(eval((byteExpr / longExprs(0)) == ByteExpression(1)))
    assert(eval((byteExpr / intExprs(0)) == ByteExpression(1)))
    assert(eval((byteExpr / charExprs(0)) == ByteExpression(1)))
    assert(eval((byteExpr / shortExprs(0)) == ByteExpression(1)))
    assert(eval((byteExpr / byteExprs(0)) == ByteExpression(1)))
    assert(eval((byteExpr / doubleExprs(1)) == DoubleExpression(0.5)))
    assert(eval((byteExpr / floatExprs(1)) == DoubleExpression(0.5)))
    assert(eval((byteExpr / longExprs(1)) == DoubleExpression(0)))
    assert(eval((byteExpr / intExprs(1)) == DoubleExpression(0)))
    assert(eval((byteExpr / charExprs(1)) == DoubleExpression(0)))
    assert(eval((byteExpr / shortExprs(1)) == DoubleExpression(0)))
    assert(eval((byteExpr / byteExprs(1)) == DoubleExpression(0)))

    byteExpr = ByteExpression(3)
    assert(eval((byteExpr % doubleExprs(0)) == ByteExpression(0)))
    assert(eval((byteExpr % floatExprs(0)) == ByteExpression(0)))
    assert(eval((byteExpr % longExprs(0)) == ByteExpression(0)))
    assert(eval((byteExpr % intExprs(0)) == ByteExpression(0)))
    assert(eval((byteExpr % charExprs(0)) == ByteExpression(0)))
    assert(eval((byteExpr % shortExprs(0)) == ByteExpression(0)))
    assert(eval((byteExpr % byteExprs(0)) == ByteExpression(0)))
    assert(eval((byteExpr % doubleExprs(1)) == ByteExpression(1)))
    assert(eval((byteExpr % floatExprs(1)) == ByteExpression(1)))
    assert(eval((byteExpr % longExprs(1)) == ByteExpression(1)))
    assert(eval((byteExpr % intExprs(1)) == ByteExpression(1)))
    assert(eval((byteExpr % charExprs(1)) == ByteExpression(1)))
    assert(eval((byteExpr % shortExprs(1)) == ByteExpression(1)))
    assert(eval((byteExpr % byteExprs(1)) == ByteExpression(1)))
  }
  test("CharExpressions evaluate properly.") {
    def eval(b:BooleanExpression): Boolean = {
      b.evaluate.get
    }
    var expr:CharExpression = CharExpression(1)

    val doubleExprs = List(DoubleExpression(1), DoubleExpression(2))
    val floatExprs = List(FloatExpression(1), FloatExpression(2))
    val longExprs = List(LongExpression(1), LongExpression(2))
    val intExprs = List(IntExpression(1), IntExpression(2))
    val charExprs = List(CharExpression(1), CharExpression(2))
    val shortExprs = List(ShortExpression(1), ShortExpression(2))
    val byteExprs = List(ByteExpression(1), ByteExpression(2))

    // Comparison Operators
    assert(eval((expr == doubleExprs(0)) && !(expr == doubleExprs(1))))
    assert(eval((expr == floatExprs(0)) && !(expr == floatExprs(1))))
    assert(eval((expr == longExprs(0)) && !(expr == longExprs(1))))
    assert(eval((expr == intExprs(0)) && !(expr == intExprs(1))))
    assert(eval((expr == charExprs(0)) && !(expr == charExprs(1))))
    assert(eval((expr == shortExprs(0)) && !(expr == shortExprs(1))))
    assert(eval((expr == byteExprs(0)) && !(expr == byteExprs(1))))

    assert(eval(!(expr != doubleExprs(0)) && (expr != doubleExprs(1))))
    assert(eval(!(expr != floatExprs(0)) && (expr != floatExprs(1))))
    assert(eval(!(expr != longExprs(0)) && (expr != longExprs(1))))
    assert(eval(!(expr != intExprs(0)) && (expr != intExprs(1))))
    assert(eval(!(expr != charExprs(0)) && (expr != charExprs(1))))
    assert(eval(!(expr != shortExprs(0)) && (expr != shortExprs(1))))
    assert(eval(!(expr != byteExprs(0)) && (expr != byteExprs(1))))

    assert(eval(!(expr > doubleExprs(0)) && !(expr > doubleExprs(1))))
    assert(eval(!(expr > floatExprs(0)) && !(expr > floatExprs(1))))
    assert(eval(!(expr > longExprs(0)) && !(expr > longExprs(1))))
    assert(eval(!(expr > intExprs(0)) && !(expr > intExprs(1))))
    assert(eval(!(expr > charExprs(0)) && !(expr > charExprs(1))))
    assert(eval(!(expr > shortExprs(0)) && !(expr > shortExprs(1))))
    assert(eval(!(expr > byteExprs(0)) && !(expr > byteExprs(1))))

    assert(eval((expr >= doubleExprs(0)) && !(expr >= doubleExprs(1))))
    assert(eval((expr >= floatExprs(0)) && !(expr >= floatExprs(1))))
    assert(eval((expr >= longExprs(0)) && !(expr >= longExprs(1))))
    assert(eval((expr >= intExprs(0)) && !(expr >= intExprs(1))))
    assert(eval((expr >= charExprs(0)) && !(expr >= charExprs(1))))
    assert(eval((expr >= shortExprs(0)) && !(expr >= shortExprs(1))))
    assert(eval((expr >= byteExprs(0)) && !(expr >= byteExprs(1))))

    assert(eval(!(expr < doubleExprs(0)) && (expr < doubleExprs(1))))
    assert(eval(!(expr < floatExprs(0)) && (expr < floatExprs(1))))
    assert(eval(!(expr < longExprs(0)) && (expr < longExprs(1))))
    assert(eval(!(expr < intExprs(0)) && (expr < intExprs(1))))
    assert(eval(!(expr < charExprs(0)) && (expr < charExprs(1))))
    assert(eval(!(expr < shortExprs(0)) && (expr < shortExprs(1))))
    assert(eval(!(expr < byteExprs(0)) && (expr < byteExprs(1))))

    assert(eval((expr <= doubleExprs(0)) && (expr <= doubleExprs(1))))
    assert(eval((expr <= floatExprs(0)) && (expr <= floatExprs(1))))
    assert(eval((expr <= longExprs(0)) && (expr <= longExprs(1))))
    assert(eval((expr <= intExprs(0)) && (expr <= intExprs(1))))
    assert(eval((expr <= charExprs(0)) && (expr <= charExprs(1))))
    assert(eval((expr <= shortExprs(0)) && (expr <= shortExprs(1))))
    assert(eval((expr <= byteExprs(0)) && (expr <= byteExprs(1))))

    assert(+ByteExpression(1) == ByteExpression(1))
    assert(-ByteExpression(1) == ByteExpression(-1))
    assert(~ByteExpression(1) == IntExpression(~1))

    assert(eval((expr + doubleExprs(0)) == ByteExpression(2)))
    assert(eval((expr + floatExprs(0)) == ByteExpression(2)))
    assert(eval((expr + longExprs(0)) == ByteExpression(2)))
    assert(eval((expr + intExprs(0)) == ByteExpression(2)))
    assert(eval((expr + charExprs(0)) == ByteExpression(2)))
    assert(eval((expr + shortExprs(0)) == ByteExpression(2)))
    assert(eval((expr + byteExprs(0)) == ByteExpression(2)))
    assert(eval((expr + doubleExprs(1)) == ByteExpression(3)))
    assert(eval((expr + floatExprs(1)) == ByteExpression(3)))
    assert(eval((expr + longExprs(1)) == ByteExpression(3)))
    assert(eval((expr + intExprs(1)) == ByteExpression(3)))
    assert(eval((expr + charExprs(1)) == ByteExpression(3)))
    assert(eval((expr + shortExprs(1)) == ByteExpression(3)))
    assert(eval((expr + byteExprs(1)) == ByteExpression(3)))

    assert(eval((expr - doubleExprs(0)) == ByteExpression(0)))
    assert(eval((expr - floatExprs(0)) == ByteExpression(0)))
    assert(eval((expr - longExprs(0)) == ByteExpression(0)))
    assert(eval((expr - intExprs(0)) == ByteExpression(0)))
    assert(eval((expr - charExprs(0)) == ByteExpression(0)))
    assert(eval((expr - shortExprs(0)) == ByteExpression(0)))
    assert(eval((expr - byteExprs(0)) == ByteExpression(0)))
    assert(eval((expr - doubleExprs(1)) == IntExpression(-1)))
    assert(eval((expr - floatExprs(1)) == IntExpression(-1)))
    assert(eval((expr - longExprs(1)) == IntExpression(-1)))
    assert(eval((expr - intExprs(1)) == IntExpression(-1)))
    assert(eval((expr - charExprs(1)) == IntExpression(-1)))
    assert(eval((expr - shortExprs(1)) == IntExpression(-1)))
    assert(eval((expr - byteExprs(1)) == IntExpression(-1)))

    assert(eval((expr * doubleExprs(0)) == ByteExpression(1)))
    assert(eval((expr * floatExprs(0)) == ByteExpression(1)))
    assert(eval((expr * longExprs(0)) == ByteExpression(1)))
    assert(eval((expr * intExprs(0)) == ByteExpression(1)))
    assert(eval((expr * charExprs(0)) == ByteExpression(1)))
    assert(eval((expr * shortExprs(0)) == ByteExpression(1)))
    assert(eval((expr * byteExprs(0)) == ByteExpression(1)))
    assert(eval((expr * doubleExprs(1)) == ByteExpression(2)))
    assert(eval((expr * floatExprs(1)) == ByteExpression(2)))
    assert(eval((expr * longExprs(1)) == ByteExpression(2)))
    assert(eval((expr * intExprs(1)) == ByteExpression(2)))
    assert(eval((expr * charExprs(1)) == ByteExpression(2)))
    assert(eval((expr * shortExprs(1)) == ByteExpression(2)))
    assert(eval((expr * byteExprs(1)) == ByteExpression(2)))

    assert(eval((expr / doubleExprs(0)) == ByteExpression(1)))
    assert(eval((expr / floatExprs(0)) == ByteExpression(1)))
    assert(eval((expr / longExprs(0)) == ByteExpression(1)))
    assert(eval((expr / intExprs(0)) == ByteExpression(1)))
    assert(eval((expr / charExprs(0)) == ByteExpression(1)))
    assert(eval((expr / shortExprs(0)) == ByteExpression(1)))
    assert(eval((expr / byteExprs(0)) == ByteExpression(1)))
    assert(eval((expr / doubleExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / floatExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / longExprs(1)) == DoubleExpression(0)))
    assert(eval((expr / intExprs(1)) == DoubleExpression(0)))
    assert(eval((expr / charExprs(1)) == DoubleExpression(0)))
    assert(eval((expr / shortExprs(1)) == DoubleExpression(0)))
    assert(eval((expr / byteExprs(1)) == DoubleExpression(0)))

    expr = CharExpression(3)
    assert(eval((expr % doubleExprs(0)) == ByteExpression(0)))
    assert(eval((expr % floatExprs(0)) == ByteExpression(0)))
    assert(eval((expr % longExprs(0)) == ByteExpression(0)))
    assert(eval((expr % intExprs(0)) == ByteExpression(0)))
    assert(eval((expr % charExprs(0)) == ByteExpression(0)))
    assert(eval((expr % shortExprs(0)) == ByteExpression(0)))
    assert(eval((expr % byteExprs(0)) == ByteExpression(0)))
    assert(eval((expr % doubleExprs(1)) == ByteExpression(1)))
    assert(eval((expr % floatExprs(1)) == ByteExpression(1)))
    assert(eval((expr % longExprs(1)) == ByteExpression(1)))
    assert(eval((expr % intExprs(1)) == ByteExpression(1)))
    assert(eval((expr % charExprs(1)) == ByteExpression(1)))
    assert(eval((expr % shortExprs(1)) == ByteExpression(1)))
    assert(eval((expr % byteExprs(1)) == ByteExpression(1)))
  }
  test("DoubleExpressions evaluate properly.") {
    def eval(b:BooleanExpression): Boolean = {
      b.evaluate.get
    }
    var expr:DoubleExpression = DoubleExpression(1.0)

    val doubleExprs = List(DoubleExpression(1), DoubleExpression(2))
    val floatExprs = List(FloatExpression(1), FloatExpression(2))
    val longExprs = List(LongExpression(1), LongExpression(2))
    val intExprs = List(IntExpression(1), IntExpression(2))
    val charExprs = List(CharExpression(1), CharExpression(2))
    val shortExprs = List(ShortExpression(1), ShortExpression(2))
    val byteExprs = List(ByteExpression(1), ByteExpression(2))

    // Comparison Operators
    assert(eval((expr == doubleExprs(0)) && !(expr == doubleExprs(1))))
    assert(eval((expr == floatExprs(0)) && !(expr == floatExprs(1))))
    assert(eval((expr == longExprs(0)) && !(expr == longExprs(1))))
    assert(eval((expr == intExprs(0)) && !(expr == intExprs(1))))
    assert(eval((expr == charExprs(0)) && !(expr == charExprs(1))))
    assert(eval((expr == shortExprs(0)) && !(expr == shortExprs(1))))
    assert(eval((expr == byteExprs(0)) && !(expr == byteExprs(1))))

    assert(eval(!(expr != doubleExprs(0)) && (expr != doubleExprs(1))))
    assert(eval(!(expr != floatExprs(0)) && (expr != floatExprs(1))))
    assert(eval(!(expr != longExprs(0)) && (expr != longExprs(1))))
    assert(eval(!(expr != intExprs(0)) && (expr != intExprs(1))))
    assert(eval(!(expr != charExprs(0)) && (expr != charExprs(1))))
    assert(eval(!(expr != shortExprs(0)) && (expr != shortExprs(1))))
    assert(eval(!(expr != byteExprs(0)) && (expr != byteExprs(1))))

    assert(eval(!(expr > doubleExprs(0)) && !(expr > doubleExprs(1))))
    assert(eval(!(expr > floatExprs(0)) && !(expr > floatExprs(1))))
    assert(eval(!(expr > longExprs(0)) && !(expr > longExprs(1))))
    assert(eval(!(expr > intExprs(0)) && !(expr > intExprs(1))))
    assert(eval(!(expr > charExprs(0)) && !(expr > charExprs(1))))
    assert(eval(!(expr > shortExprs(0)) && !(expr > shortExprs(1))))
    assert(eval(!(expr > byteExprs(0)) && !(expr > byteExprs(1))))

    assert(eval((expr >= doubleExprs(0)) && !(expr >= doubleExprs(1))))
    assert(eval((expr >= floatExprs(0)) && !(expr >= floatExprs(1))))
    assert(eval((expr >= longExprs(0)) && !(expr >= longExprs(1))))
    assert(eval((expr >= intExprs(0)) && !(expr >= intExprs(1))))
    assert(eval((expr >= charExprs(0)) && !(expr >= charExprs(1))))
    assert(eval((expr >= shortExprs(0)) && !(expr >= shortExprs(1))))
    assert(eval((expr >= byteExprs(0)) && !(expr >= byteExprs(1))))

    assert(eval(!(expr < doubleExprs(0)) && (expr < doubleExprs(1))))
    assert(eval(!(expr < floatExprs(0)) && (expr < floatExprs(1))))
    assert(eval(!(expr < longExprs(0)) && (expr < longExprs(1))))
    assert(eval(!(expr < intExprs(0)) && (expr < intExprs(1))))
    assert(eval(!(expr < charExprs(0)) && (expr < charExprs(1))))
    assert(eval(!(expr < shortExprs(0)) && (expr < shortExprs(1))))
    assert(eval(!(expr < byteExprs(0)) && (expr < byteExprs(1))))

    assert(eval((expr <= doubleExprs(0)) && (expr <= doubleExprs(1))))
    assert(eval((expr <= floatExprs(0)) && (expr <= floatExprs(1))))
    assert(eval((expr <= longExprs(0)) && (expr <= longExprs(1))))
    assert(eval((expr <= intExprs(0)) && (expr <= intExprs(1))))
    assert(eval((expr <= charExprs(0)) && (expr <= charExprs(1))))
    assert(eval((expr <= shortExprs(0)) && (expr <= shortExprs(1))))
    assert(eval((expr <= byteExprs(0)) && (expr <= byteExprs(1))))

    assert(+ByteExpression(1) == ByteExpression(1))
    assert(-ByteExpression(1) == ByteExpression(-1))
    assert(~ByteExpression(1) == IntExpression(~1))

    assert(eval((expr + doubleExprs(0)) == ByteExpression(2)))
    assert(eval((expr + floatExprs(0)) == ByteExpression(2)))
    assert(eval((expr + longExprs(0)) == ByteExpression(2)))
    assert(eval((expr + intExprs(0)) == ByteExpression(2)))
    assert(eval((expr + charExprs(0)) == ByteExpression(2)))
    assert(eval((expr + shortExprs(0)) == ByteExpression(2)))
    assert(eval((expr + byteExprs(0)) == ByteExpression(2)))
    assert(eval((expr + doubleExprs(1)) == ByteExpression(3)))
    assert(eval((expr + floatExprs(1)) == ByteExpression(3)))
    assert(eval((expr + longExprs(1)) == ByteExpression(3)))
    assert(eval((expr + intExprs(1)) == ByteExpression(3)))
    assert(eval((expr + charExprs(1)) == ByteExpression(3)))
    assert(eval((expr + shortExprs(1)) == ByteExpression(3)))
    assert(eval((expr + byteExprs(1)) == ByteExpression(3)))

    assert(eval((expr - doubleExprs(0)) == ByteExpression(0)))
    assert(eval((expr - floatExprs(0)) == ByteExpression(0)))
    assert(eval((expr - longExprs(0)) == ByteExpression(0)))
    assert(eval((expr - intExprs(0)) == ByteExpression(0)))
    assert(eval((expr - charExprs(0)) == ByteExpression(0)))
    assert(eval((expr - shortExprs(0)) == ByteExpression(0)))
    assert(eval((expr - byteExprs(0)) == ByteExpression(0)))
    assert(eval((expr - doubleExprs(1)) == IntExpression(-1)))
    assert(eval((expr - floatExprs(1)) == IntExpression(-1)))
    assert(eval((expr - longExprs(1)) == IntExpression(-1)))
    assert(eval((expr - intExprs(1)) == IntExpression(-1)))
    assert(eval((expr - charExprs(1)) == IntExpression(-1)))
    assert(eval((expr - shortExprs(1)) == IntExpression(-1)))
    assert(eval((expr - byteExprs(1)) == IntExpression(-1)))

    assert(eval((expr * doubleExprs(0)) == ByteExpression(1)))
    assert(eval((expr * floatExprs(0)) == ByteExpression(1)))
    assert(eval((expr * longExprs(0)) == ByteExpression(1)))
    assert(eval((expr * intExprs(0)) == ByteExpression(1)))
    assert(eval((expr * charExprs(0)) == ByteExpression(1)))
    assert(eval((expr * shortExprs(0)) == ByteExpression(1)))
    assert(eval((expr * byteExprs(0)) == ByteExpression(1)))
    assert(eval((expr * doubleExprs(1)) == ByteExpression(2)))
    assert(eval((expr * floatExprs(1)) == ByteExpression(2)))
    assert(eval((expr * longExprs(1)) == ByteExpression(2)))
    assert(eval((expr * intExprs(1)) == ByteExpression(2)))
    assert(eval((expr * charExprs(1)) == ByteExpression(2)))
    assert(eval((expr * shortExprs(1)) == ByteExpression(2)))
    assert(eval((expr * byteExprs(1)) == ByteExpression(2)))

    assert(eval((expr / doubleExprs(0)) == ByteExpression(1)))
    assert(eval((expr / floatExprs(0)) == ByteExpression(1)))
    assert(eval((expr / longExprs(0)) == ByteExpression(1)))
    assert(eval((expr / intExprs(0)) == ByteExpression(1)))
    assert(eval((expr / charExprs(0)) == ByteExpression(1)))
    assert(eval((expr / shortExprs(0)) == ByteExpression(1)))
    assert(eval((expr / byteExprs(0)) == ByteExpression(1)))
    assert(eval((expr / doubleExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / floatExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / longExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / intExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / charExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / shortExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / byteExprs(1)) == DoubleExpression(0.5)))

    expr = DoubleExpression(3.0)
    assert(eval((expr % doubleExprs(0)) == ByteExpression(0)))
    assert(eval((expr % floatExprs(0)) == ByteExpression(0)))
    assert(eval((expr % longExprs(0)) == ByteExpression(0)))
    assert(eval((expr % intExprs(0)) == ByteExpression(0)))
    assert(eval((expr % charExprs(0)) == ByteExpression(0)))
    assert(eval((expr % shortExprs(0)) == ByteExpression(0)))
    assert(eval((expr % byteExprs(0)) == ByteExpression(0)))
    assert(eval((expr % doubleExprs(1)) == ByteExpression(1)))
    assert(eval((expr % floatExprs(1)) == ByteExpression(1)))
    assert(eval((expr % longExprs(1)) == ByteExpression(1)))
    assert(eval((expr % intExprs(1)) == ByteExpression(1)))
    assert(eval((expr % charExprs(1)) == ByteExpression(1)))
    assert(eval((expr % shortExprs(1)) == ByteExpression(1)))
    assert(eval((expr % byteExprs(1)) == ByteExpression(1)))
  }
  test("FloatExpressions evaluate properly.") {
    def eval(b:BooleanExpression): Boolean = {
      b.evaluate.get
    }
    var expr:FloatExpression = FloatExpression(1.0F)

    val doubleExprs = List(DoubleExpression(1), DoubleExpression(2))
    val floatExprs = List(FloatExpression(1), FloatExpression(2))
    val longExprs = List(LongExpression(1), LongExpression(2))
    val intExprs = List(IntExpression(1), IntExpression(2))
    val charExprs = List(CharExpression(1), CharExpression(2))
    val shortExprs = List(ShortExpression(1), ShortExpression(2))
    val byteExprs = List(ByteExpression(1), ByteExpression(2))

    // Comparison Operators
    assert(eval((expr == doubleExprs(0)) && !(expr == doubleExprs(1))))
    assert(eval((expr == floatExprs(0)) && !(expr == floatExprs(1))))
    assert(eval((expr == longExprs(0)) && !(expr == longExprs(1))))
    assert(eval((expr == intExprs(0)) && !(expr == intExprs(1))))
    assert(eval((expr == charExprs(0)) && !(expr == charExprs(1))))
    assert(eval((expr == shortExprs(0)) && !(expr == shortExprs(1))))
    assert(eval((expr == byteExprs(0)) && !(expr == byteExprs(1))))

    assert(eval(!(expr != doubleExprs(0)) && (expr != doubleExprs(1))))
    assert(eval(!(expr != floatExprs(0)) && (expr != floatExprs(1))))
    assert(eval(!(expr != longExprs(0)) && (expr != longExprs(1))))
    assert(eval(!(expr != intExprs(0)) && (expr != intExprs(1))))
    assert(eval(!(expr != charExprs(0)) && (expr != charExprs(1))))
    assert(eval(!(expr != shortExprs(0)) && (expr != shortExprs(1))))
    assert(eval(!(expr != byteExprs(0)) && (expr != byteExprs(1))))

    assert(eval(!(expr > doubleExprs(0)) && !(expr > doubleExprs(1))))
    assert(eval(!(expr > floatExprs(0)) && !(expr > floatExprs(1))))
    assert(eval(!(expr > longExprs(0)) && !(expr > longExprs(1))))
    assert(eval(!(expr > intExprs(0)) && !(expr > intExprs(1))))
    assert(eval(!(expr > charExprs(0)) && !(expr > charExprs(1))))
    assert(eval(!(expr > shortExprs(0)) && !(expr > shortExprs(1))))
    assert(eval(!(expr > byteExprs(0)) && !(expr > byteExprs(1))))

    assert(eval((expr >= doubleExprs(0)) && !(expr >= doubleExprs(1))))
    assert(eval((expr >= floatExprs(0)) && !(expr >= floatExprs(1))))
    assert(eval((expr >= longExprs(0)) && !(expr >= longExprs(1))))
    assert(eval((expr >= intExprs(0)) && !(expr >= intExprs(1))))
    assert(eval((expr >= charExprs(0)) && !(expr >= charExprs(1))))
    assert(eval((expr >= shortExprs(0)) && !(expr >= shortExprs(1))))
    assert(eval((expr >= byteExprs(0)) && !(expr >= byteExprs(1))))

    assert(eval(!(expr < doubleExprs(0)) && (expr < doubleExprs(1))))
    assert(eval(!(expr < floatExprs(0)) && (expr < floatExprs(1))))
    assert(eval(!(expr < longExprs(0)) && (expr < longExprs(1))))
    assert(eval(!(expr < intExprs(0)) && (expr < intExprs(1))))
    assert(eval(!(expr < charExprs(0)) && (expr < charExprs(1))))
    assert(eval(!(expr < shortExprs(0)) && (expr < shortExprs(1))))
    assert(eval(!(expr < byteExprs(0)) && (expr < byteExprs(1))))

    assert(eval((expr <= doubleExprs(0)) && (expr <= doubleExprs(1))))
    assert(eval((expr <= floatExprs(0)) && (expr <= floatExprs(1))))
    assert(eval((expr <= longExprs(0)) && (expr <= longExprs(1))))
    assert(eval((expr <= intExprs(0)) && (expr <= intExprs(1))))
    assert(eval((expr <= charExprs(0)) && (expr <= charExprs(1))))
    assert(eval((expr <= shortExprs(0)) && (expr <= shortExprs(1))))
    assert(eval((expr <= byteExprs(0)) && (expr <= byteExprs(1))))

    assert(+ByteExpression(1) == ByteExpression(1))
    assert(-ByteExpression(1) == ByteExpression(-1))
    assert(~ByteExpression(1) == IntExpression(~1))

    assert(eval((expr + doubleExprs(0)) == ByteExpression(2)))
    assert(eval((expr + floatExprs(0)) == ByteExpression(2)))
    assert(eval((expr + longExprs(0)) == ByteExpression(2)))
    assert(eval((expr + intExprs(0)) == ByteExpression(2)))
    assert(eval((expr + charExprs(0)) == ByteExpression(2)))
    assert(eval((expr + shortExprs(0)) == ByteExpression(2)))
    assert(eval((expr + byteExprs(0)) == ByteExpression(2)))
    assert(eval((expr + doubleExprs(1)) == ByteExpression(3)))
    assert(eval((expr + floatExprs(1)) == ByteExpression(3)))
    assert(eval((expr + longExprs(1)) == ByteExpression(3)))
    assert(eval((expr + intExprs(1)) == ByteExpression(3)))
    assert(eval((expr + charExprs(1)) == ByteExpression(3)))
    assert(eval((expr + shortExprs(1)) == ByteExpression(3)))
    assert(eval((expr + byteExprs(1)) == ByteExpression(3)))

    assert(eval((expr - doubleExprs(0)) == ByteExpression(0)))
    assert(eval((expr - floatExprs(0)) == ByteExpression(0)))
    assert(eval((expr - longExprs(0)) == ByteExpression(0)))
    assert(eval((expr - intExprs(0)) == ByteExpression(0)))
    assert(eval((expr - charExprs(0)) == ByteExpression(0)))
    assert(eval((expr - shortExprs(0)) == ByteExpression(0)))
    assert(eval((expr - byteExprs(0)) == ByteExpression(0)))
    assert(eval((expr - doubleExprs(1)) == IntExpression(-1)))
    assert(eval((expr - floatExprs(1)) == IntExpression(-1)))
    assert(eval((expr - longExprs(1)) == IntExpression(-1)))
    assert(eval((expr - intExprs(1)) == IntExpression(-1)))
    assert(eval((expr - charExprs(1)) == IntExpression(-1)))
    assert(eval((expr - shortExprs(1)) == IntExpression(-1)))
    assert(eval((expr - byteExprs(1)) == IntExpression(-1)))

    assert(eval((expr * doubleExprs(0)) == ByteExpression(1)))
    assert(eval((expr * floatExprs(0)) == ByteExpression(1)))
    assert(eval((expr * longExprs(0)) == ByteExpression(1)))
    assert(eval((expr * intExprs(0)) == ByteExpression(1)))
    assert(eval((expr * charExprs(0)) == ByteExpression(1)))
    assert(eval((expr * shortExprs(0)) == ByteExpression(1)))
    assert(eval((expr * byteExprs(0)) == ByteExpression(1)))
    assert(eval((expr * doubleExprs(1)) == ByteExpression(2)))
    assert(eval((expr * floatExprs(1)) == ByteExpression(2)))
    assert(eval((expr * longExprs(1)) == ByteExpression(2)))
    assert(eval((expr * intExprs(1)) == ByteExpression(2)))
    assert(eval((expr * charExprs(1)) == ByteExpression(2)))
    assert(eval((expr * shortExprs(1)) == ByteExpression(2)))
    assert(eval((expr * byteExprs(1)) == ByteExpression(2)))

    assert(eval((expr / doubleExprs(0)) == ByteExpression(1)))
    assert(eval((expr / floatExprs(0)) == ByteExpression(1)))
    assert(eval((expr / longExprs(0)) == ByteExpression(1)))
    assert(eval((expr / intExprs(0)) == ByteExpression(1)))
    assert(eval((expr / charExprs(0)) == ByteExpression(1)))
    assert(eval((expr / shortExprs(0)) == ByteExpression(1)))
    assert(eval((expr / byteExprs(0)) == ByteExpression(1)))
    assert(eval((expr / doubleExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / floatExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / longExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / intExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / charExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / shortExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / byteExprs(1)) == DoubleExpression(0.5)))

    expr = FloatExpression(3.0F)
    assert(eval((expr % doubleExprs(0)) == ByteExpression(0)))
    assert(eval((expr % floatExprs(0)) == ByteExpression(0)))
    assert(eval((expr % longExprs(0)) == ByteExpression(0)))
    assert(eval((expr % intExprs(0)) == ByteExpression(0)))
    assert(eval((expr % charExprs(0)) == ByteExpression(0)))
    assert(eval((expr % shortExprs(0)) == ByteExpression(0)))
    assert(eval((expr % byteExprs(0)) == ByteExpression(0)))
    assert(eval((expr % doubleExprs(1)) == ByteExpression(1)))
    assert(eval((expr % floatExprs(1)) == ByteExpression(1)))
    assert(eval((expr % longExprs(1)) == ByteExpression(1)))
    assert(eval((expr % intExprs(1)) == ByteExpression(1)))
    assert(eval((expr % charExprs(1)) == ByteExpression(1)))
    assert(eval((expr % shortExprs(1)) == ByteExpression(1)))
    assert(eval((expr % byteExprs(1)) == ByteExpression(1)))
  }
  test("IntExpressions evaluate properly.") {
    def eval(b:BooleanExpression): Boolean = {
      b.evaluate.get
    }
    var expr:IntExpression = IntExpression(1)

    val doubleExprs = List(DoubleExpression(1), DoubleExpression(2))
    val floatExprs = List(FloatExpression(1), FloatExpression(2))
    val longExprs = List(LongExpression(1), LongExpression(2))
    val intExprs = List(IntExpression(1), IntExpression(2))
    val charExprs = List(CharExpression(1), CharExpression(2))
    val shortExprs = List(ShortExpression(1), ShortExpression(2))
    val byteExprs = List(ByteExpression(1), ByteExpression(2))

    // Comparison Operators
    assert(eval((expr == doubleExprs(0)) && !(expr == doubleExprs(1))))
    assert(eval((expr == floatExprs(0)) && !(expr == floatExprs(1))))
    assert(eval((expr == longExprs(0)) && !(expr == longExprs(1))))
    assert(eval((expr == intExprs(0)) && !(expr == intExprs(1))))
    assert(eval((expr == charExprs(0)) && !(expr == charExprs(1))))
    assert(eval((expr == shortExprs(0)) && !(expr == shortExprs(1))))
    assert(eval((expr == byteExprs(0)) && !(expr == byteExprs(1))))

    assert(eval(!(expr != doubleExprs(0)) && (expr != doubleExprs(1))))
    assert(eval(!(expr != floatExprs(0)) && (expr != floatExprs(1))))
    assert(eval(!(expr != longExprs(0)) && (expr != longExprs(1))))
    assert(eval(!(expr != intExprs(0)) && (expr != intExprs(1))))
    assert(eval(!(expr != charExprs(0)) && (expr != charExprs(1))))
    assert(eval(!(expr != shortExprs(0)) && (expr != shortExprs(1))))
    assert(eval(!(expr != byteExprs(0)) && (expr != byteExprs(1))))

    assert(eval(!(expr > doubleExprs(0)) && !(expr > doubleExprs(1))))
    assert(eval(!(expr > floatExprs(0)) && !(expr > floatExprs(1))))
    assert(eval(!(expr > longExprs(0)) && !(expr > longExprs(1))))
    assert(eval(!(expr > intExprs(0)) && !(expr > intExprs(1))))
    assert(eval(!(expr > charExprs(0)) && !(expr > charExprs(1))))
    assert(eval(!(expr > shortExprs(0)) && !(expr > shortExprs(1))))
    assert(eval(!(expr > byteExprs(0)) && !(expr > byteExprs(1))))

    assert(eval((expr >= doubleExprs(0)) && !(expr >= doubleExprs(1))))
    assert(eval((expr >= floatExprs(0)) && !(expr >= floatExprs(1))))
    assert(eval((expr >= longExprs(0)) && !(expr >= longExprs(1))))
    assert(eval((expr >= intExprs(0)) && !(expr >= intExprs(1))))
    assert(eval((expr >= charExprs(0)) && !(expr >= charExprs(1))))
    assert(eval((expr >= shortExprs(0)) && !(expr >= shortExprs(1))))
    assert(eval((expr >= byteExprs(0)) && !(expr >= byteExprs(1))))

    assert(eval(!(expr < doubleExprs(0)) && (expr < doubleExprs(1))))
    assert(eval(!(expr < floatExprs(0)) && (expr < floatExprs(1))))
    assert(eval(!(expr < longExprs(0)) && (expr < longExprs(1))))
    assert(eval(!(expr < intExprs(0)) && (expr < intExprs(1))))
    assert(eval(!(expr < charExprs(0)) && (expr < charExprs(1))))
    assert(eval(!(expr < shortExprs(0)) && (expr < shortExprs(1))))
    assert(eval(!(expr < byteExprs(0)) && (expr < byteExprs(1))))

    assert(eval((expr <= doubleExprs(0)) && (expr <= doubleExprs(1))))
    assert(eval((expr <= floatExprs(0)) && (expr <= floatExprs(1))))
    assert(eval((expr <= longExprs(0)) && (expr <= longExprs(1))))
    assert(eval((expr <= intExprs(0)) && (expr <= intExprs(1))))
    assert(eval((expr <= charExprs(0)) && (expr <= charExprs(1))))
    assert(eval((expr <= shortExprs(0)) && (expr <= shortExprs(1))))
    assert(eval((expr <= byteExprs(0)) && (expr <= byteExprs(1))))

    assert(+ByteExpression(1) == ByteExpression(1))
    assert(-ByteExpression(1) == ByteExpression(-1))
    assert(~ByteExpression(1) == IntExpression(~1))

    assert(eval((expr + doubleExprs(0)) == ByteExpression(2)))
    assert(eval((expr + floatExprs(0)) == ByteExpression(2)))
    assert(eval((expr + longExprs(0)) == ByteExpression(2)))
    assert(eval((expr + intExprs(0)) == ByteExpression(2)))
    assert(eval((expr + charExprs(0)) == ByteExpression(2)))
    assert(eval((expr + shortExprs(0)) == ByteExpression(2)))
    assert(eval((expr + byteExprs(0)) == ByteExpression(2)))
    assert(eval((expr + doubleExprs(1)) == ByteExpression(3)))
    assert(eval((expr + floatExprs(1)) == ByteExpression(3)))
    assert(eval((expr + longExprs(1)) == ByteExpression(3)))
    assert(eval((expr + intExprs(1)) == ByteExpression(3)))
    assert(eval((expr + charExprs(1)) == ByteExpression(3)))
    assert(eval((expr + shortExprs(1)) == ByteExpression(3)))
    assert(eval((expr + byteExprs(1)) == ByteExpression(3)))

    assert(eval((expr - doubleExprs(0)) == ByteExpression(0)))
    assert(eval((expr - floatExprs(0)) == ByteExpression(0)))
    assert(eval((expr - longExprs(0)) == ByteExpression(0)))
    assert(eval((expr - intExprs(0)) == ByteExpression(0)))
    assert(eval((expr - charExprs(0)) == ByteExpression(0)))
    assert(eval((expr - shortExprs(0)) == ByteExpression(0)))
    assert(eval((expr - byteExprs(0)) == ByteExpression(0)))
    assert(eval((expr - doubleExprs(1)) == IntExpression(-1)))
    assert(eval((expr - floatExprs(1)) == IntExpression(-1)))
    assert(eval((expr - longExprs(1)) == IntExpression(-1)))
    assert(eval((expr - intExprs(1)) == IntExpression(-1)))
    assert(eval((expr - charExprs(1)) == IntExpression(-1)))
    assert(eval((expr - shortExprs(1)) == IntExpression(-1)))
    assert(eval((expr - byteExprs(1)) == IntExpression(-1)))

    assert(eval((expr * doubleExprs(0)) == ByteExpression(1)))
    assert(eval((expr * floatExprs(0)) == ByteExpression(1)))
    assert(eval((expr * longExprs(0)) == ByteExpression(1)))
    assert(eval((expr * intExprs(0)) == ByteExpression(1)))
    assert(eval((expr * charExprs(0)) == ByteExpression(1)))
    assert(eval((expr * shortExprs(0)) == ByteExpression(1)))
    assert(eval((expr * byteExprs(0)) == ByteExpression(1)))
    assert(eval((expr * doubleExprs(1)) == ByteExpression(2)))
    assert(eval((expr * floatExprs(1)) == ByteExpression(2)))
    assert(eval((expr * longExprs(1)) == ByteExpression(2)))
    assert(eval((expr * intExprs(1)) == ByteExpression(2)))
    assert(eval((expr * charExprs(1)) == ByteExpression(2)))
    assert(eval((expr * shortExprs(1)) == ByteExpression(2)))
    assert(eval((expr * byteExprs(1)) == ByteExpression(2)))

    assert(eval((expr / doubleExprs(0)) == ByteExpression(1)))
    assert(eval((expr / floatExprs(0)) == ByteExpression(1)))
    assert(eval((expr / longExprs(0)) == ByteExpression(1)))
    assert(eval((expr / intExprs(0)) == ByteExpression(1)))
    assert(eval((expr / charExprs(0)) == ByteExpression(1)))
    assert(eval((expr / shortExprs(0)) == ByteExpression(1)))
    assert(eval((expr / byteExprs(0)) == ByteExpression(1)))
    assert(eval((expr / doubleExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / floatExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / longExprs(1)) == DoubleExpression(0)))
    assert(eval((expr / intExprs(1)) == DoubleExpression(0)))
    assert(eval((expr / charExprs(1)) == DoubleExpression(0)))
    assert(eval((expr / shortExprs(1)) == DoubleExpression(0)))
    assert(eval((expr / byteExprs(1)) == DoubleExpression(0)))

    expr = IntExpression(3)
    assert(eval((expr % doubleExprs(0)) == ByteExpression(0)))
    assert(eval((expr % floatExprs(0)) == ByteExpression(0)))
    assert(eval((expr % longExprs(0)) == ByteExpression(0)))
    assert(eval((expr % intExprs(0)) == ByteExpression(0)))
    assert(eval((expr % charExprs(0)) == ByteExpression(0)))
    assert(eval((expr % shortExprs(0)) == ByteExpression(0)))
    assert(eval((expr % byteExprs(0)) == ByteExpression(0)))
    assert(eval((expr % doubleExprs(1)) == ByteExpression(1)))
    assert(eval((expr % floatExprs(1)) == ByteExpression(1)))
    assert(eval((expr % longExprs(1)) == ByteExpression(1)))
    assert(eval((expr % intExprs(1)) == ByteExpression(1)))
    assert(eval((expr % charExprs(1)) == ByteExpression(1)))
    assert(eval((expr % shortExprs(1)) == ByteExpression(1)))
    assert(eval((expr % byteExprs(1)) == ByteExpression(1)))
  }
  test("LongExpressions evaluate properly.") {
    def eval(b:BooleanExpression): Boolean = {
      b.evaluate.get
    }
    var expr:LongExpression = LongExpression(1L)

    val doubleExprs = List(DoubleExpression(1), DoubleExpression(2))
    val floatExprs = List(FloatExpression(1), FloatExpression(2))
    val longExprs = List(LongExpression(1), LongExpression(2))
    val intExprs = List(IntExpression(1), IntExpression(2))
    val charExprs = List(CharExpression(1), CharExpression(2))
    val shortExprs = List(ShortExpression(1), ShortExpression(2))
    val byteExprs = List(ByteExpression(1), ByteExpression(2))

    // Comparison Operators
    assert(eval((expr == doubleExprs(0)) && !(expr == doubleExprs(1))))
    assert(eval((expr == floatExprs(0)) && !(expr == floatExprs(1))))
    assert(eval((expr == longExprs(0)) && !(expr == longExprs(1))))
    assert(eval((expr == intExprs(0)) && !(expr == intExprs(1))))
    assert(eval((expr == charExprs(0)) && !(expr == charExprs(1))))
    assert(eval((expr == shortExprs(0)) && !(expr == shortExprs(1))))
    assert(eval((expr == byteExprs(0)) && !(expr == byteExprs(1))))

    assert(eval(!(expr != doubleExprs(0)) && (expr != doubleExprs(1))))
    assert(eval(!(expr != floatExprs(0)) && (expr != floatExprs(1))))
    assert(eval(!(expr != longExprs(0)) && (expr != longExprs(1))))
    assert(eval(!(expr != intExprs(0)) && (expr != intExprs(1))))
    assert(eval(!(expr != charExprs(0)) && (expr != charExprs(1))))
    assert(eval(!(expr != shortExprs(0)) && (expr != shortExprs(1))))
    assert(eval(!(expr != byteExprs(0)) && (expr != byteExprs(1))))

    assert(eval(!(expr > doubleExprs(0)) && !(expr > doubleExprs(1))))
    assert(eval(!(expr > floatExprs(0)) && !(expr > floatExprs(1))))
    assert(eval(!(expr > longExprs(0)) && !(expr > longExprs(1))))
    assert(eval(!(expr > intExprs(0)) && !(expr > intExprs(1))))
    assert(eval(!(expr > charExprs(0)) && !(expr > charExprs(1))))
    assert(eval(!(expr > shortExprs(0)) && !(expr > shortExprs(1))))
    assert(eval(!(expr > byteExprs(0)) && !(expr > byteExprs(1))))

    assert(eval((expr >= doubleExprs(0)) && !(expr >= doubleExprs(1))))
    assert(eval((expr >= floatExprs(0)) && !(expr >= floatExprs(1))))
    assert(eval((expr >= longExprs(0)) && !(expr >= longExprs(1))))
    assert(eval((expr >= intExprs(0)) && !(expr >= intExprs(1))))
    assert(eval((expr >= charExprs(0)) && !(expr >= charExprs(1))))
    assert(eval((expr >= shortExprs(0)) && !(expr >= shortExprs(1))))
    assert(eval((expr >= byteExprs(0)) && !(expr >= byteExprs(1))))

    assert(eval(!(expr < doubleExprs(0)) && (expr < doubleExprs(1))))
    assert(eval(!(expr < floatExprs(0)) && (expr < floatExprs(1))))
    assert(eval(!(expr < longExprs(0)) && (expr < longExprs(1))))
    assert(eval(!(expr < intExprs(0)) && (expr < intExprs(1))))
    assert(eval(!(expr < charExprs(0)) && (expr < charExprs(1))))
    assert(eval(!(expr < shortExprs(0)) && (expr < shortExprs(1))))
    assert(eval(!(expr < byteExprs(0)) && (expr < byteExprs(1))))

    assert(eval((expr <= doubleExprs(0)) && (expr <= doubleExprs(1))))
    assert(eval((expr <= floatExprs(0)) && (expr <= floatExprs(1))))
    assert(eval((expr <= longExprs(0)) && (expr <= longExprs(1))))
    assert(eval((expr <= intExprs(0)) && (expr <= intExprs(1))))
    assert(eval((expr <= charExprs(0)) && (expr <= charExprs(1))))
    assert(eval((expr <= shortExprs(0)) && (expr <= shortExprs(1))))
    assert(eval((expr <= byteExprs(0)) && (expr <= byteExprs(1))))

    assert(+ByteExpression(1) == ByteExpression(1))
    assert(-ByteExpression(1) == ByteExpression(-1))
    assert(~ByteExpression(1) == IntExpression(~1))

    assert(eval((expr + doubleExprs(0)) == ByteExpression(2)))
    assert(eval((expr + floatExprs(0)) == ByteExpression(2)))
    assert(eval((expr + longExprs(0)) == ByteExpression(2)))
    assert(eval((expr + intExprs(0)) == ByteExpression(2)))
    assert(eval((expr + charExprs(0)) == ByteExpression(2)))
    assert(eval((expr + shortExprs(0)) == ByteExpression(2)))
    assert(eval((expr + byteExprs(0)) == ByteExpression(2)))
    assert(eval((expr + doubleExprs(1)) == ByteExpression(3)))
    assert(eval((expr + floatExprs(1)) == ByteExpression(3)))
    assert(eval((expr + longExprs(1)) == ByteExpression(3)))
    assert(eval((expr + intExprs(1)) == ByteExpression(3)))
    assert(eval((expr + charExprs(1)) == ByteExpression(3)))
    assert(eval((expr + shortExprs(1)) == ByteExpression(3)))
    assert(eval((expr + byteExprs(1)) == ByteExpression(3)))

    assert(eval((expr - doubleExprs(0)) == ByteExpression(0)))
    assert(eval((expr - floatExprs(0)) == ByteExpression(0)))
    assert(eval((expr - longExprs(0)) == ByteExpression(0)))
    assert(eval((expr - intExprs(0)) == ByteExpression(0)))
    assert(eval((expr - charExprs(0)) == ByteExpression(0)))
    assert(eval((expr - shortExprs(0)) == ByteExpression(0)))
    assert(eval((expr - byteExprs(0)) == ByteExpression(0)))
    assert(eval((expr - doubleExprs(1)) == IntExpression(-1)))
    assert(eval((expr - floatExprs(1)) == IntExpression(-1)))
    assert(eval((expr - longExprs(1)) == IntExpression(-1)))
    assert(eval((expr - intExprs(1)) == IntExpression(-1)))
    assert(eval((expr - charExprs(1)) == IntExpression(-1)))
    assert(eval((expr - shortExprs(1)) == IntExpression(-1)))
    assert(eval((expr - byteExprs(1)) == IntExpression(-1)))

    assert(eval((expr * doubleExprs(0)) == ByteExpression(1)))
    assert(eval((expr * floatExprs(0)) == ByteExpression(1)))
    assert(eval((expr * longExprs(0)) == ByteExpression(1)))
    assert(eval((expr * intExprs(0)) == ByteExpression(1)))
    assert(eval((expr * charExprs(0)) == ByteExpression(1)))
    assert(eval((expr * shortExprs(0)) == ByteExpression(1)))
    assert(eval((expr * byteExprs(0)) == ByteExpression(1)))
    assert(eval((expr * doubleExprs(1)) == ByteExpression(2)))
    assert(eval((expr * floatExprs(1)) == ByteExpression(2)))
    assert(eval((expr * longExprs(1)) == ByteExpression(2)))
    assert(eval((expr * intExprs(1)) == ByteExpression(2)))
    assert(eval((expr * charExprs(1)) == ByteExpression(2)))
    assert(eval((expr * shortExprs(1)) == ByteExpression(2)))
    assert(eval((expr * byteExprs(1)) == ByteExpression(2)))

    assert(eval((expr / doubleExprs(0)) == ByteExpression(1)))
    assert(eval((expr / floatExprs(0)) == ByteExpression(1)))
    assert(eval((expr / longExprs(0)) == ByteExpression(1)))
    assert(eval((expr / intExprs(0)) == ByteExpression(1)))
    assert(eval((expr / charExprs(0)) == ByteExpression(1)))
    assert(eval((expr / shortExprs(0)) == ByteExpression(1)))
    assert(eval((expr / byteExprs(0)) == ByteExpression(1)))
    assert(eval((expr / doubleExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / floatExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / longExprs(1)) == DoubleExpression(0)))
    assert(eval((expr / intExprs(1)) == DoubleExpression(0)))
    assert(eval((expr / charExprs(1)) == DoubleExpression(0)))
    assert(eval((expr / shortExprs(1)) == DoubleExpression(0)))
    assert(eval((expr / byteExprs(1)) == DoubleExpression(0)))

    expr = LongExpression(3L)
    assert(eval((expr % doubleExprs(0)) == ByteExpression(0)))
    assert(eval((expr % floatExprs(0)) == ByteExpression(0)))
    assert(eval((expr % longExprs(0)) == ByteExpression(0)))
    assert(eval((expr % intExprs(0)) == ByteExpression(0)))
    assert(eval((expr % charExprs(0)) == ByteExpression(0)))
    assert(eval((expr % shortExprs(0)) == ByteExpression(0)))
    assert(eval((expr % byteExprs(0)) == ByteExpression(0)))
    assert(eval((expr % doubleExprs(1)) == ByteExpression(1)))
    assert(eval((expr % floatExprs(1)) == ByteExpression(1)))
    assert(eval((expr % longExprs(1)) == ByteExpression(1)))
    assert(eval((expr % intExprs(1)) == ByteExpression(1)))
    assert(eval((expr % charExprs(1)) == ByteExpression(1)))
    assert(eval((expr % shortExprs(1)) == ByteExpression(1)))
    assert(eval((expr % byteExprs(1)) == ByteExpression(1)))
  }
  test("ShortExpressions evaluate properly.") {
    def eval(b:BooleanExpression): Boolean = {
      b.evaluate.get
    }
    var expr:ShortExpression = ShortExpression(1)

    val doubleExprs = List(DoubleExpression(1), DoubleExpression(2))
    val floatExprs = List(FloatExpression(1), FloatExpression(2))
    val longExprs = List(LongExpression(1), LongExpression(2))
    val intExprs = List(IntExpression(1), IntExpression(2))
    val charExprs = List(CharExpression(1), CharExpression(2))
    val shortExprs = List(ShortExpression(1), ShortExpression(2))
    val byteExprs = List(ByteExpression(1), ByteExpression(2))

    // Comparison Operators
    assert(eval((expr == doubleExprs(0)) && !(expr == doubleExprs(1))))
    assert(eval((expr == floatExprs(0)) && !(expr == floatExprs(1))))
    assert(eval((expr == longExprs(0)) && !(expr == longExprs(1))))
    assert(eval((expr == intExprs(0)) && !(expr == intExprs(1))))
    assert(eval((expr == charExprs(0)) && !(expr == charExprs(1))))
    assert(eval((expr == shortExprs(0)) && !(expr == shortExprs(1))))
    assert(eval((expr == byteExprs(0)) && !(expr == byteExprs(1))))

    assert(eval(!(expr != doubleExprs(0)) && (expr != doubleExprs(1))))
    assert(eval(!(expr != floatExprs(0)) && (expr != floatExprs(1))))
    assert(eval(!(expr != longExprs(0)) && (expr != longExprs(1))))
    assert(eval(!(expr != intExprs(0)) && (expr != intExprs(1))))
    assert(eval(!(expr != charExprs(0)) && (expr != charExprs(1))))
    assert(eval(!(expr != shortExprs(0)) && (expr != shortExprs(1))))
    assert(eval(!(expr != byteExprs(0)) && (expr != byteExprs(1))))

    assert(eval(!(expr > doubleExprs(0)) && !(expr > doubleExprs(1))))
    assert(eval(!(expr > floatExprs(0)) && !(expr > floatExprs(1))))
    assert(eval(!(expr > longExprs(0)) && !(expr > longExprs(1))))
    assert(eval(!(expr > intExprs(0)) && !(expr > intExprs(1))))
    assert(eval(!(expr > charExprs(0)) && !(expr > charExprs(1))))
    assert(eval(!(expr > shortExprs(0)) && !(expr > shortExprs(1))))
    assert(eval(!(expr > byteExprs(0)) && !(expr > byteExprs(1))))

    assert(eval((expr >= doubleExprs(0)) && !(expr >= doubleExprs(1))))
    assert(eval((expr >= floatExprs(0)) && !(expr >= floatExprs(1))))
    assert(eval((expr >= longExprs(0)) && !(expr >= longExprs(1))))
    assert(eval((expr >= intExprs(0)) && !(expr >= intExprs(1))))
    assert(eval((expr >= charExprs(0)) && !(expr >= charExprs(1))))
    assert(eval((expr >= shortExprs(0)) && !(expr >= shortExprs(1))))
    assert(eval((expr >= byteExprs(0)) && !(expr >= byteExprs(1))))

    assert(eval(!(expr < doubleExprs(0)) && (expr < doubleExprs(1))))
    assert(eval(!(expr < floatExprs(0)) && (expr < floatExprs(1))))
    assert(eval(!(expr < longExprs(0)) && (expr < longExprs(1))))
    assert(eval(!(expr < intExprs(0)) && (expr < intExprs(1))))
    assert(eval(!(expr < charExprs(0)) && (expr < charExprs(1))))
    assert(eval(!(expr < shortExprs(0)) && (expr < shortExprs(1))))
    assert(eval(!(expr < byteExprs(0)) && (expr < byteExprs(1))))

    assert(eval((expr <= doubleExprs(0)) && (expr <= doubleExprs(1))))
    assert(eval((expr <= floatExprs(0)) && (expr <= floatExprs(1))))
    assert(eval((expr <= longExprs(0)) && (expr <= longExprs(1))))
    assert(eval((expr <= intExprs(0)) && (expr <= intExprs(1))))
    assert(eval((expr <= charExprs(0)) && (expr <= charExprs(1))))
    assert(eval((expr <= shortExprs(0)) && (expr <= shortExprs(1))))
    assert(eval((expr <= byteExprs(0)) && (expr <= byteExprs(1))))

    assert(+ByteExpression(1) == ByteExpression(1))
    assert(-ByteExpression(1) == ByteExpression(-1))
    assert(~ByteExpression(1) == IntExpression(~1))

    assert(eval((expr + doubleExprs(0)) == ByteExpression(2)))
    assert(eval((expr + floatExprs(0)) == ByteExpression(2)))
    assert(eval((expr + longExprs(0)) == ByteExpression(2)))
    assert(eval((expr + intExprs(0)) == ByteExpression(2)))
    assert(eval((expr + charExprs(0)) == ByteExpression(2)))
    assert(eval((expr + shortExprs(0)) == ByteExpression(2)))
    assert(eval((expr + byteExprs(0)) == ByteExpression(2)))
    assert(eval((expr + doubleExprs(1)) == ByteExpression(3)))
    assert(eval((expr + floatExprs(1)) == ByteExpression(3)))
    assert(eval((expr + longExprs(1)) == ByteExpression(3)))
    assert(eval((expr + intExprs(1)) == ByteExpression(3)))
    assert(eval((expr + charExprs(1)) == ByteExpression(3)))
    assert(eval((expr + shortExprs(1)) == ByteExpression(3)))
    assert(eval((expr + byteExprs(1)) == ByteExpression(3)))

    assert(eval((expr - doubleExprs(0)) == ByteExpression(0)))
    assert(eval((expr - floatExprs(0)) == ByteExpression(0)))
    assert(eval((expr - longExprs(0)) == ByteExpression(0)))
    assert(eval((expr - intExprs(0)) == ByteExpression(0)))
    assert(eval((expr - charExprs(0)) == ByteExpression(0)))
    assert(eval((expr - shortExprs(0)) == ByteExpression(0)))
    assert(eval((expr - byteExprs(0)) == ByteExpression(0)))
    assert(eval((expr - doubleExprs(1)) == IntExpression(-1)))
    assert(eval((expr - floatExprs(1)) == IntExpression(-1)))
    assert(eval((expr - longExprs(1)) == IntExpression(-1)))
    assert(eval((expr - intExprs(1)) == IntExpression(-1)))
    assert(eval((expr - charExprs(1)) == IntExpression(-1)))
    assert(eval((expr - shortExprs(1)) == IntExpression(-1)))
    assert(eval((expr - byteExprs(1)) == IntExpression(-1)))

    assert(eval((expr * doubleExprs(0)) == ByteExpression(1)))
    assert(eval((expr * floatExprs(0)) == ByteExpression(1)))
    assert(eval((expr * longExprs(0)) == ByteExpression(1)))
    assert(eval((expr * intExprs(0)) == ByteExpression(1)))
    assert(eval((expr * charExprs(0)) == ByteExpression(1)))
    assert(eval((expr * shortExprs(0)) == ByteExpression(1)))
    assert(eval((expr * byteExprs(0)) == ByteExpression(1)))
    assert(eval((expr * doubleExprs(1)) == ByteExpression(2)))
    assert(eval((expr * floatExprs(1)) == ByteExpression(2)))
    assert(eval((expr * longExprs(1)) == ByteExpression(2)))
    assert(eval((expr * intExprs(1)) == ByteExpression(2)))
    assert(eval((expr * charExprs(1)) == ByteExpression(2)))
    assert(eval((expr * shortExprs(1)) == ByteExpression(2)))
    assert(eval((expr * byteExprs(1)) == ByteExpression(2)))

    assert(eval((expr / doubleExprs(0)) == ByteExpression(1)))
    assert(eval((expr / floatExprs(0)) == ByteExpression(1)))
    assert(eval((expr / longExprs(0)) == ByteExpression(1)))
    assert(eval((expr / intExprs(0)) == ByteExpression(1)))
    assert(eval((expr / charExprs(0)) == ByteExpression(1)))
    assert(eval((expr / shortExprs(0)) == ByteExpression(1)))
    assert(eval((expr / byteExprs(0)) == ByteExpression(1)))
    assert(eval((expr / doubleExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / floatExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / longExprs(1)) == DoubleExpression(0)))
    assert(eval((expr / intExprs(1)) == DoubleExpression(0)))
    assert(eval((expr / charExprs(1)) == DoubleExpression(0)))
    assert(eval((expr / shortExprs(1)) == DoubleExpression(0)))
    assert(eval((expr / byteExprs(1)) == DoubleExpression(0)))

    expr = ShortExpression(3)
    assert(eval((expr % doubleExprs(0)) == ByteExpression(0)))
    assert(eval((expr % floatExprs(0)) == ByteExpression(0)))
    assert(eval((expr % longExprs(0)) == ByteExpression(0)))
    assert(eval((expr % intExprs(0)) == ByteExpression(0)))
    assert(eval((expr % charExprs(0)) == ByteExpression(0)))
    assert(eval((expr % shortExprs(0)) == ByteExpression(0)))
    assert(eval((expr % byteExprs(0)) == ByteExpression(0)))
    assert(eval((expr % doubleExprs(1)) == ByteExpression(1)))
    assert(eval((expr % floatExprs(1)) == ByteExpression(1)))
    assert(eval((expr % longExprs(1)) == ByteExpression(1)))
    assert(eval((expr % intExprs(1)) == ByteExpression(1)))
    assert(eval((expr % charExprs(1)) == ByteExpression(1)))
    assert(eval((expr % shortExprs(1)) == ByteExpression(1)))
    assert(eval((expr % byteExprs(1)) == ByteExpression(1)))
  }
  test("BooleanExpressions evaluate properly.") {
    val trueExpression = BooleanExpression(true)
    val falseExpression = BooleanExpression(false)

    assert(trueExpression.evaluate.get)
    assert(!(falseExpression.evaluate.get))
  }
  test("BooleanExpression operators evaluate properly.") {
    val trueExpression = BooleanExpression(true)
    val falseExpression = BooleanExpression(false)

    assert((!(trueExpression != trueExpression).evaluate.get))
    assert((trueExpression != falseExpression).evaluate.get)
    assert((falseExpression != trueExpression).evaluate.get)
    assert((!(falseExpression != falseExpression).evaluate.get))
    (falseExpression != falseExpression) match {
      case _:Operator_!=[_, _, _, _] => ()
      case _ => fail
    }
    (falseExpression == falseExpression) match {
      case _:Operator_==[_, _, _, _] => ()
      case _ => fail
    }

    assert((trueExpression & trueExpression).evaluate.get)
    assert((!(trueExpression & falseExpression).evaluate.get))
    assert((!(falseExpression & trueExpression).evaluate.get))
    assert((!(falseExpression & falseExpression).evaluate.get))

    assert((trueExpression && trueExpression).evaluate.get)
    assert((!(trueExpression && falseExpression).evaluate.get))
    assert((!(falseExpression && trueExpression).evaluate.get))
    assert((!(falseExpression && falseExpression).evaluate.get))

    assert((trueExpression == trueExpression).evaluate.get)
    assert((!(trueExpression == falseExpression).evaluate.get))
    assert((!(falseExpression == trueExpression).evaluate.get))
    assert((falseExpression == falseExpression).evaluate.get)

    assert((!(trueExpression ^ trueExpression).evaluate.get))
    assert((trueExpression ^ falseExpression).evaluate.get)
    assert((falseExpression ^ trueExpression).evaluate.get)
    assert((!(falseExpression ^ falseExpression).evaluate.get))

    assert((!(!trueExpression).evaluate.get))
    assert((!falseExpression).evaluate.get)

    assert((trueExpression | trueExpression).evaluate.get)
    assert((trueExpression | falseExpression).evaluate.get)
    assert((falseExpression | trueExpression).evaluate.get)
    assert((!(falseExpression | falseExpression).evaluate.get))

    assert((trueExpression || trueExpression).evaluate.get)
    assert((trueExpression || falseExpression).evaluate.get)
    assert((falseExpression || trueExpression).evaluate.get)
    assert((!(falseExpression || falseExpression).evaluate.get))

    assert((!(trueExpression < trueExpression).evaluate.get))
    assert((!(trueExpression < falseExpression).evaluate.get))
    assert((falseExpression < trueExpression).evaluate.get)
    assert((!(falseExpression < falseExpression).evaluate.get))

    assert((trueExpression <= trueExpression).evaluate.get)
    assert((!(trueExpression <= falseExpression).evaluate.get))
    assert((falseExpression <= trueExpression).evaluate.get)
    assert((falseExpression <= falseExpression).evaluate.get)

    assert((!(trueExpression > trueExpression).evaluate.get))
    assert((trueExpression > falseExpression).evaluate.get)
    assert((!(falseExpression > trueExpression).evaluate.get))
    assert((!(falseExpression > falseExpression).evaluate.get))

    assert((trueExpression >= trueExpression).evaluate.get)
    assert((trueExpression >= falseExpression).evaluate.get)
    assert((!(falseExpression >= trueExpression).evaluate.get))
    assert((falseExpression >= falseExpression).evaluate.get)

//    assert(trueExpression.compareTo(trueExpression).evaluate.get == true.compareTo(true))
//    assert(trueExpression.compareTo(falseExpression).evaluate.get == true.compareTo(false))
//    assert(falseExpression.compareTo(trueExpression).evaluate.get == false.compareTo(true))
//    assert(falseExpression.compareTo(falseExpression).evaluate.get == false.compareTo(false))
//
//    assert(trueExpression.compare(trueExpression).evaluate.get == true.compare(true))
//    assert(trueExpression.compare(falseExpression).evaluate.get == true.compare(false))
//    assert(falseExpression.compare(trueExpression).evaluate.get == false.compare(true))
//    assert(falseExpression.compare(falseExpression).evaluate.get == false.compare(false))
  }
  test("Primitive IntExpression evaluate properly for a few different literals") {
    for (i <- 0 until 100) {
      val int = IntExpression(i)
      assert(int.evaluate.get == i)
    }
  }
  test("basic iff expressions (no otherwise) work") {
    val iffTrueExpression = iff (BooleanExpression(true)) {
      BooleanExpression(true)
    }.build
    assert(iffTrueExpression.evaluate.get)

    val iffFalseExpression = iff (BooleanExpression(false)) {
      BooleanExpression(true)
    }.build
    assert(iffFalseExpression.evaluate match {
      case Some(_) => fail
      case None => true
    })
  }
  test("iff expressions with otherwise expressions") {
    val iffTrueExpressionResult = iff (BooleanExpression(true)) {
      BooleanExpression(true)
    } otherwise {
      BooleanExpression(false)
    }
    assert(iffTrueExpressionResult.build.evaluate.get)

    val iffFalseExpressionResult = iff (BooleanExpression(false)) {
      BooleanExpression(true)
    } otherwise {
      BooleanExpression(false)
    }
    assert(!iffFalseExpressionResult.build.evaluate.get)
  }
  test("iff expressions with otherwise iff expressions") {
    var int = IntExpression(1)
    val iffExpression1 = iff (int == IntExpression(1)) {
      IntExpression(1)
    } otherwise iff (int == IntExpression(2)) {
      IntExpression(2)
    } otherwise iff (int == IntExpression(3)) {
      IntExpression(3)
    }
    assert(iffExpression1.build.evaluate.get == 1)

    int = IntExpression(2)
    val iffExpression2 = iff(int == IntExpression(1)) {
      IntExpression(1)
    } otherwise iff(int == IntExpression(2)) {
      IntExpression(2)
    } otherwise iff(int == IntExpression(3)) {
      IntExpression(3)
    }
    assert(iffExpression2.build.evaluate.get == 2)

    int = IntExpression(3)
    val iffExpression3 = iff (int == IntExpression(1)) {
      IntExpression(1)
    } otherwise iff (int == IntExpression(2)) {
      IntExpression(2)
    } otherwise iff (int == IntExpression(3)) {
      IntExpression(3)
    }
    assert(iffExpression3.build.evaluate.get == 3)
  }
  test("test type conversion") {
    import org.leisurely_script.gdl.ImplicitDefs.Views.TypeConversions._
    def convertMe[V <: AnyVal, T <: AnyValExpression[V]](expr:T):Boolean = {
      expr match {
        case de:T => true
        case _ => fail
      }
    }

    val byte:Byte = 1
    val long:Long = 1
    val short:Short = 1

    assert(convertMe[Boolean, BooleanExpression](true))
    assert(convertMe[Byte, ByteExpression](byte))
    assert(convertMe[Char, CharExpression]('a'))
    assert(convertMe[Double, DoubleExpression](1.0))
    assert(convertMe[Float, FloatExpression](1.0F))
    assert(convertMe[Int, IntExpression](1))
    assert(convertMe[Long, LongExpression](long))
    assert(convertMe[Short, ShortExpression](short))
  }
  test("Does an EndCondition evaluate properly?") {
    val player1:SpecificPlayer = SpecificPlayer(Player("1"))
    val endCondition = EndCondition(iff(true) {
      player1.wins
    })
    endCondition.result.evaluate match {
      case Some(sGR:SGameResult) => {
        assert(sGR.result == Win)
        sGR.players match {
          case p:ConcretelyKnownPlayer => assert(player1.getPlayers == p.getPlayers)
          case _ => fail("the player was not correct")
        }
      }
      case None => fail(s"endCondition.evaluate returned None")
    }
  }
  test("A proper AST is generated for GameExpressions") {
    val expr = BooleanExpression(true) && BooleanExpression(true)
  }
  test("Test that GameExpressions are properly covariant") {
    val bE1:BooleanExpression = BooleanExpression(true)
    assert(bE1.isInstanceOf[GameExpression[Boolean]])
    assert(bE1.isInstanceOf[GameExpression[AnyVal]])
    assert(bE1.isInstanceOf[GameExpression[Any]])
  }
  test("BooleanExpressions return method call objects") {
    val bE1:BooleanBinaryOperator = BooleanExpression(true) && BooleanExpression(true)
  }
}
