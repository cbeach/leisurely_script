package org.leisurely_script.gdl.types


trait GameExpression[T] {
  def evaluate:Option[T]
}
