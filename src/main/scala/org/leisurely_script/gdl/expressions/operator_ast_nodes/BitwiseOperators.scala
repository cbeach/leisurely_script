package org.leisurely_script.gdl.expressions.operator_ast_nodes

import org.leisurely_script.gdl.types._

private[gdl] object BitwiseOperators {
  case class Operator_&(left: LongExpression, right: LongExpression) extends LongExpression {
    override def evaluate:Option[Long] = Some(left.evaluate.get & right.evaluate.get)
  }
  case class Operator_|(left: LongExpression, right: LongExpression) extends LongExpression {
    override def evaluate:Option[Long] = Some(left.evaluate.get | right.evaluate.get)
  }
  case class Operator_^(left: LongExpression, right: LongExpression) extends LongExpression {
    override def evaluate:Option[Long] = Some(left.evaluate.get ^ right.evaluate.get)
  }
  case class Operator_>>(left: LongExpression, right: LongExpression) extends LongExpression {
    override def evaluate:Option[Long] = Some(left.evaluate.get >> right.evaluate.get)
  }
  case class Operator_<<(left: LongExpression, right: LongExpression) extends LongExpression {
    override def evaluate:Option[Long] = Some(left.evaluate.get << right.evaluate.get)
  }
  case class Operator_>>>(left: LongExpression, right: LongExpression) extends LongExpression {
    override def evaluate:Option[Long] = Some(left.evaluate.get >>> right.evaluate.get)
  }
  case class UnaryOperator_~(self: LongExpression) extends LongExpression {
    override def evaluate:Option[Long] = Some(~self.evaluate.get)
  }
}

