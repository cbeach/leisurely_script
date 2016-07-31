package org.leisurely_script.gdl

import org.leisurely_script.gdl.expressions.operator_ast_nodes.EquivalenceOperators.{Operator_==, Operator_!=}

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite

import org.leisurely_script.gdl.types._
import org.leisurely_script.gdl.expressions.iff
import org.leisurely_script.gdl.GameResultState._
import org.leisurely_script.gdl.ImplicitDefs.Views.TypeConversions._


class DLSTests extends FunSuite {
  test("DoubleExpressions evaluate properly.") {
    def eval(b:BooleanExpression): Boolean = {
      b.evaluate.get
    }
    var expr:DoubleExpression = DoubleExpression(1.0)

    val doubleExprs = List(DoubleExpression(1), DoubleExpression(2))
    val longExprs = List(LongExpression(1), LongExpression(2))

    // Comparison Operators
    assert(eval((expr == doubleExprs(0)) && !(expr == doubleExprs(1))))
    assert(eval((expr == longExprs(0)) && !(expr == longExprs(1))))

    assert(eval(!(expr != doubleExprs(0)) && (expr != doubleExprs(1))))
    assert(eval(!(expr != longExprs(0)) && (expr != longExprs(1))))

    assert(eval(!(expr > doubleExprs(0)) && !(expr > doubleExprs(1))))
    assert(eval(!(expr > longExprs(0)) && !(expr > longExprs(1))))

    assert(eval((expr >= doubleExprs(0)) && !(expr >= doubleExprs(1))))
    assert(eval((expr >= longExprs(0)) && !(expr >= longExprs(1))))

    assert(eval(!(expr < doubleExprs(0)) && (expr < doubleExprs(1))))
    assert(eval(!(expr < longExprs(0)) && (expr < longExprs(1))))

    assert(eval((expr <= doubleExprs(0)) && (expr <= doubleExprs(1))))
    assert(eval((expr <= longExprs(0)) && (expr <= longExprs(1))))


    assert(eval((expr / doubleExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / longExprs(1)) == DoubleExpression(0.5)))

    expr = DoubleExpression(3.0)
  }
  test("LongExpressions evaluate properly.") {
    def eval(b:BooleanExpression): Boolean = {
      b.evaluate.get
    }
    var expr:LongExpression = LongExpression(1L)

    val doubleExprs = List(DoubleExpression(1), DoubleExpression(2))
    val longExprs = List(LongExpression(1), LongExpression(2))

    // Comparison Operators
    assert(eval((expr == doubleExprs(0)) && !(expr == doubleExprs(1))))
    assert(eval((expr == longExprs(0)) && !(expr == longExprs(1))))

    assert(eval(!(expr != doubleExprs(0)) && (expr != doubleExprs(1))))
    assert(eval(!(expr != longExprs(0)) && (expr != longExprs(1))))

    assert(eval(!(expr > doubleExprs(0)) && !(expr > doubleExprs(1))))
    assert(eval(!(expr > longExprs(0)) && !(expr > longExprs(1))))

    assert(eval((expr >= doubleExprs(0)) && !(expr >= doubleExprs(1))))
    assert(eval((expr >= longExprs(0)) && !(expr >= longExprs(1))))

    assert(eval(!(expr < doubleExprs(0)) && (expr < doubleExprs(1))))
    assert(eval(!(expr < longExprs(0)) && (expr < longExprs(1))))

    assert(eval((expr <= doubleExprs(0)) && (expr <= doubleExprs(1))))
    assert(eval((expr <= longExprs(0)) && (expr <= longExprs(1))))

    assert(eval((expr / doubleExprs(1)) == DoubleExpression(0.5)))
    assert(eval((expr / longExprs(1)) == DoubleExpression(0)))

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
      case _:Operator_!=[_, _] => ()
      case _ => fail
    }
    (falseExpression == falseExpression) match {
      case _:Operator_==[_, _] => ()
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
    var int = LongExpression(1)
    val iffExpression1 = iff (int == LongExpression(1)) {
      LongExpression(1)
    } otherwise iff (int == LongExpression(2)) {
      LongExpression(2)
    } otherwise iff (int == LongExpression(3)) {
      LongExpression(3)
    }
    assert(iffExpression1.build.evaluate.get == 1)

    int = LongExpression(2)
    val iffExpression2 = iff(int == LongExpression(1)) {
      LongExpression(1)
    } otherwise iff(int == LongExpression(2)) {
      LongExpression(2)
    } otherwise iff(int == LongExpression(3)) {
      LongExpression(3)
    }
    assert(iffExpression2.build.evaluate.get == 2)

    int = LongExpression(3)
    val iffExpression3 = iff (int == LongExpression(1)) {
      LongExpression(1)
    } otherwise iff (int == LongExpression(2)) {
      LongExpression(2)
    } otherwise iff (int == LongExpression(3)) {
      LongExpression(3)
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
    assert(convertMe[Double, DoubleExpression](1.0))
    assert(convertMe[Long, LongExpression](long))
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
}
