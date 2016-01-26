package org.leisurely_script

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite

import org.leisurely_script.gdl.types._
import org.leisurelyscript.gdl._


class DLSTests extends FunSuite {
  test("BooleanExpressions evaluate properly.") {
    val trueExpression = BooleanExpression(true)
    val falseExpression = BooleanExpression(false)

    assert(trueExpression.evaluate)
    assert(!(falseExpression.evaluate))
  }
  test("BooleanExpression operators evaluate properly.") {
    val trueExpression = BooleanExpression(true)
    val falseExpression = BooleanExpression(false)

    assert(!(trueExpression != trueExpression).evaluate)
    assert((trueExpression != falseExpression).evaluate)
    assert((falseExpression != trueExpression).evaluate)
    assert(!(falseExpression != falseExpression).evaluate)

    assert((trueExpression & trueExpression).evaluate)
    assert(!(trueExpression & falseExpression).evaluate)
    assert(!(falseExpression & trueExpression).evaluate)
    assert(!(falseExpression & falseExpression).evaluate)

    assert((trueExpression && trueExpression).evaluate)
    assert(!(trueExpression && falseExpression).evaluate)
    assert(!(falseExpression && trueExpression).evaluate)
    assert(!(falseExpression && falseExpression).evaluate)

    assert((trueExpression == trueExpression).evaluate)
    assert(!(trueExpression == falseExpression).evaluate)
    assert(!(falseExpression == trueExpression).evaluate)
    assert((falseExpression == falseExpression).evaluate)

    assert(!(trueExpression ^ trueExpression).evaluate)
    assert((trueExpression ^ falseExpression).evaluate)
    assert((falseExpression ^ trueExpression).evaluate)
    assert(!(falseExpression ^ falseExpression).evaluate)

    assert(!(!trueExpression).evaluate)
    assert((!falseExpression).evaluate)

    assert((trueExpression | trueExpression).evaluate)
    assert((trueExpression | falseExpression).evaluate)
    assert((falseExpression | trueExpression).evaluate)
    assert(!(falseExpression | falseExpression).evaluate)

    assert((trueExpression || trueExpression).evaluate)
    assert((trueExpression || falseExpression).evaluate)
    assert((falseExpression || trueExpression).evaluate)
    assert(!(falseExpression || falseExpression).evaluate)

    assert(!(trueExpression < trueExpression).evaluate)
    assert(!(trueExpression < falseExpression).evaluate)
    assert((falseExpression < trueExpression).evaluate)
    assert(!(falseExpression < falseExpression).evaluate)

    assert((trueExpression <= trueExpression).evaluate)
    assert(!(trueExpression <= falseExpression).evaluate)
    assert((falseExpression <= trueExpression).evaluate)
    assert((falseExpression <= falseExpression).evaluate)

    assert(!(trueExpression > trueExpression).evaluate)
    assert((trueExpression > falseExpression).evaluate)
    assert(!(falseExpression > trueExpression).evaluate)
    assert(!(falseExpression > falseExpression).evaluate)

    assert((trueExpression >= trueExpression).evaluate)
    assert((trueExpression >= falseExpression).evaluate)
    assert(!(falseExpression >= trueExpression).evaluate)
    assert((falseExpression >= falseExpression).evaluate)

    assert(trueExpression.compareTo(trueExpression).evaluate == true.compareTo(true))
    assert(trueExpression.compareTo(falseExpression).evaluate == true.compareTo(false))
    assert(falseExpression.compareTo(trueExpression).evaluate == false.compareTo(true))
    assert(falseExpression.compareTo(falseExpression).evaluate == false.compareTo(false))

    assert(trueExpression.compare(trueExpression).evaluate == true.compare(true))
    assert(trueExpression.compare(falseExpression).evaluate == true.compare(false))
    assert(falseExpression.compare(trueExpression).evaluate == false.compare(true))
    assert(falseExpression.compare(falseExpression).evaluate == false.compare(false))
  }
  test("Primitive IntExpression evaluate properly for a few different literals") {
    for (i <- 0 until 100) {
      val int = IntExpression(i)
      assert(int.evaluate == i)
    }
  }
}
