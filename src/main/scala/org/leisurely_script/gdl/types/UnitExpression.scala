package org.leisurely_script.gdl.types

/**
  * Created by mcsmash on 1/29/16.
  */
case class UnitExpression[T](value:Unit = ()) extends GameExpression[T] {
  override def evaluate:Option[T] = None
}
