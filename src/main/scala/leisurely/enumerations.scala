package org.leisurelyscript


object MoveAction extends Enumeration {
    type MoveAction = Value
    val Push, Pop = Value
}


object GameResult extends Enumeration {
    type GameResult = Value
    val Win, Lose, Tie = Value
}


object Shape extends Enumeration {
    type Shape = Value
    val Triagle, Square, Rectangle, Hexagon, Octogon = Value
}


object NeighborTypes extends Enumeration {
    type NeighborTypes = Value
    val Direct, Indirect, NoDirect = Value
}
