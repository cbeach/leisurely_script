package org.leisurely_script

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite

import org.leisurely_script.gdl.types._
import org.leisurely_script.gdl.expressions.iff
import org.leisurely_script.gdl._


class DLSTests extends FunSuite {
  test("BooleanExpressions evaluate properly.") {
    val trueExpression = BooleanExpression(true)
    val falseExpression = BooleanExpression(false)

    assert(trueExpression.evaluate.get)
    assert(!(falseExpression.evaluate.get))
  }
  test("BooleanExpression operators evaluate properly.") {
    val trueExpression = BooleanExpression(true)
    val falseExpression = BooleanExpression(false)

    assert(!(trueExpression != trueExpression).evaluate.get)
    assert((trueExpression != falseExpression).evaluate.get)
    assert((falseExpression != trueExpression).evaluate.get)
    assert(!(falseExpression != falseExpression).evaluate.get)

    assert((trueExpression & trueExpression).evaluate.get)
    assert(!(trueExpression & falseExpression).evaluate.get)
    assert(!(falseExpression & trueExpression).evaluate.get)
    assert(!(falseExpression & falseExpression).evaluate.get)

    assert((trueExpression && trueExpression).evaluate.get)
    assert(!(trueExpression && falseExpression).evaluate.get)
    assert(!(falseExpression && trueExpression).evaluate.get)
    assert(!(falseExpression && falseExpression).evaluate.get)

    assert((trueExpression == trueExpression).evaluate.get)
    assert(!(trueExpression == falseExpression).evaluate.get)
    assert(!(falseExpression == trueExpression).evaluate.get)
    assert((falseExpression == falseExpression).evaluate.get)

    assert(!(trueExpression ^ trueExpression).evaluate.get)
    assert((trueExpression ^ falseExpression).evaluate.get)
    assert((falseExpression ^ trueExpression).evaluate.get)
    assert(!(falseExpression ^ falseExpression).evaluate.get)

    assert(!(!trueExpression).evaluate.get)
    assert((!falseExpression).evaluate.get)

    assert((trueExpression | trueExpression).evaluate.get)
    assert((trueExpression | falseExpression).evaluate.get)
    assert((falseExpression | trueExpression).evaluate.get)
    assert(!(falseExpression | falseExpression).evaluate.get)

    assert((trueExpression || trueExpression).evaluate.get)
    assert((trueExpression || falseExpression).evaluate.get)
    assert((falseExpression || trueExpression).evaluate.get)
    assert(!(falseExpression || falseExpression).evaluate.get)

    assert(!(trueExpression < trueExpression).evaluate.get)
    assert(!(trueExpression < falseExpression).evaluate.get)
    assert((falseExpression < trueExpression).evaluate.get)
    assert(!(falseExpression < falseExpression).evaluate.get)

    assert((trueExpression <= trueExpression).evaluate.get)
    assert(!(trueExpression <= falseExpression).evaluate.get)
    assert((falseExpression <= trueExpression).evaluate.get)
    assert((falseExpression <= falseExpression).evaluate.get)

    assert(!(trueExpression > trueExpression).evaluate.get)
    assert((trueExpression > falseExpression).evaluate.get)
    assert(!(falseExpression > trueExpression).evaluate.get)
    assert(!(falseExpression > falseExpression).evaluate.get)

    assert((trueExpression >= trueExpression).evaluate.get)
    assert((trueExpression >= falseExpression).evaluate.get)
    assert(!(falseExpression >= trueExpression).evaluate.get)
    assert((falseExpression >= falseExpression).evaluate.get)

    assert(trueExpression.compareTo(trueExpression).evaluate.get == true.compareTo(true))
    assert(trueExpression.compareTo(falseExpression).evaluate.get == true.compareTo(false))
    assert(falseExpression.compareTo(trueExpression).evaluate.get == false.compareTo(true))
    assert(falseExpression.compareTo(falseExpression).evaluate.get == false.compareTo(false))

    assert(trueExpression.compare(trueExpression).evaluate.get == true.compare(true))
    assert(trueExpression.compare(falseExpression).evaluate.get == true.compare(false))
    assert(falseExpression.compare(trueExpression).evaluate.get == false.compare(true))
    assert(falseExpression.compare(falseExpression).evaluate.get == false.compare(false))
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
}
