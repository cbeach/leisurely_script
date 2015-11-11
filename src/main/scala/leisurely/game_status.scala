package org.leisurelyscript


object GameStatus extends Enumeration {
    type GameStatus = Value
    val Invalid, Beginning, InProgress, Finished = Value
}
