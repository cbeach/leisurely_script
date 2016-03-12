package org.leisurely_script.gdl.expressions

import org.leisurely_script.gdl.types.GameExpression

/**
  * Created by mcsmash on 3/12/16.
  */
case class Function0CallExpression[+R](func:() => R) extends GameExpression[R] {
  def evaluate:Option[R] = {
    Some(func())
  }
}

abstract class Function1CallExpression[-T1, +R](arg1:T1)
  extends GameExpression[R] {}
abstract class Function2CallExpression[-T1, -T2, +R](arg1:T1, arg2:T2)
  extends GameExpression[R] {}
abstract class Function3CallExpression[-T1, -T2, -T3, +R](arg1:T1, arg2:T2, arg3:T3)
  extends GameExpression[R] {}
abstract class Function4CallExpression[-T1, -T2, -T3, -T4, +R]
  (arg1:T1, arg2:T2, arg3:T3, arg4:T4) extends GameExpression[R] {}
abstract class Function5CallExpression[-T1, -T2, -T3, -T4, -T5, +R]
  (arg1:T1, arg2:T2, arg3:T3, arg4:T4, arg5:T5) extends GameExpression[R] {}
