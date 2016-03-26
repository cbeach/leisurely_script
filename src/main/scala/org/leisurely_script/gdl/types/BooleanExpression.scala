package org.leisurely_script.gdl.types

import scala.reflect.runtime.universe._

/**
  * Created by mcsmash on 1/25/16.
  */
class BooleanExpression extends AnyValExpression[Boolean] {
  import scala.reflect.runtime.universe._
  val thisType = typeTag[BooleanExpression]
  def this(v:Boolean) = {
    this
    value = v
  }
  override def evaluate:Option[Boolean] = Some(value)
  def !=(other: BooleanExpression): BooleanExpression = {
    BooleanExpression(value != other.value)
	}
  def &(other: BooleanExpression): BooleanExpression = {
    BooleanExpression(value & other.value)
	}
  def &&(other: BooleanExpression): BooleanExpression = {
    BooleanExpression(value && other.value)
	}
  def ==(other: BooleanExpression): BooleanExpression = {
    BooleanExpression(value == other.value)
	}
  def ^(other: BooleanExpression): BooleanExpression = {
    BooleanExpression(value ^ other.value)
	}
  def unary_! = {
    BooleanExpression(!value)
	}
  def |(other: BooleanExpression): BooleanExpression = {
    BooleanExpression(value || other.value)
	}
  def ||(other: BooleanExpression): BooleanExpression = {
    BooleanExpression(value || other.value)
	}
  def <(other: BooleanExpression): BooleanExpression = {
		BooleanExpression(value < other.value)
	}
  def <=(other: BooleanExpression): BooleanExpression = {
		BooleanExpression(value <= other.value)
	}
  def >(other: BooleanExpression): BooleanExpression = {
		BooleanExpression(value > other.value)
	}
  def >=(other: BooleanExpression): BooleanExpression = {
		BooleanExpression(value >= other.value)
	}
  def compare(other: BooleanExpression): IntExpression = {
		IntExpression(value.compare(other.value))
	}
  def compareTo(other: BooleanExpression): IntExpression = {
		IntExpression(value.compareTo(other.value))
	}
}

object BooleanExpression {
  def apply() = new BooleanExpression()
  def apply(value:Boolean) = new BooleanExpression(value)
}