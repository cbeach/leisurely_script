package org.leisurely_script.gdl


object GameStatus extends Enumeration {
  type GameStatus = Value
  val WaitingToBegin, InProgress, Finished = Value
}
