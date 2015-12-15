package org.leisurelyscript.gdl


object GameStatus extends Enumeration {
  type GameStatus = Value
  val Invalid, WaitingToBegin, InProgress, Finished = Value
}
