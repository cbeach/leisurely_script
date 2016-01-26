package org.leisurely_script.gdl.types

/**
  * Created by mcsmash on 1/25/16.
  */
class GameAnyVal[T <: AnyVal](value:T) extends GameExpression[T](value) { }
