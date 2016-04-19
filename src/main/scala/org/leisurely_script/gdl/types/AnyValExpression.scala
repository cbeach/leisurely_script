package org.leisurely_script.gdl.types

/**
  * Created by mcsmash on 1/25/16.
  */
abstract class AnyValExpression[T <: AnyVal] extends GameExpression[T] with Ordered[AnyValExpression[T]] {
  def this(value:T) = {
    this
    this.setValue(value)
  }
  def setValue(v:T) = {
    value = v
  }
}
