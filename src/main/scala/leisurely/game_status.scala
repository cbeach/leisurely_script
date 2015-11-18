package org.leisurelyscript


object GameStatus extends Enumeration {
    type GameStatus = Value
    val Invalid, WaitingToBegin, InProgress, Finished = Value
}
