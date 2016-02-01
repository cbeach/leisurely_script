package org.leisurely_script.gdl.types

/**
  * Created by mcsmash on 1/27/16.
  */
class AnyRefExpression[T <: AnyRef](value:T) extends GameExpression[T] {
  override def evaluate:Option[T] = Some(value)
}
