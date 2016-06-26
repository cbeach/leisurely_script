package org.leisurely_script.gdl.types

import org.leisurely_script.gdl.GameRuleSet
import org.leisurely_script.gdl.expressions.operator_ast_nodes.EquivalenceOperators.{Operator_!=, Operator_==}
import org.leisurely_script.implementation.Game


trait GameExpression[T] {
  var value:T = _
  protected var game:Option[Game] = None
  def setGame(g:Game) = {
    game = Some(g)
  }
  def evaluate:Option[T]
  def getChildExpressions:List[GameExpression[Any]] = List()
  def gameCreationHook(gameRuleSet:GameRuleSet):Unit = {}
  def ==[U](other: GameExpression[U]): Operator_==[GameExpression[T], GameExpression[U]] = {
    Operator_==(this, other, this.equals(other))
  }
  def !=[U](other: GameExpression[U]): Operator_!=[GameExpression[T], GameExpression[U]] = {
    Operator_!=(this, other, !this.equals(other))
  }
  override def equals(o: Any): Boolean = o match {
    case o: GameExpression[_] => o.evaluate.get == value
    case _ => o == value
  }
  override def hashCode = value.hashCode()
}
