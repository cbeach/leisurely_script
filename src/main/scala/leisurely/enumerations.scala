object MoveAction extends Enumeration {
    type MoveAction = Value
    val PUSH, POP = Value
}


object GameResult extends Enumeration {
    type GameResult = Value
    val WIN, LOSE, TIE = Value
}


object Shape extends Enumeration {
    type Shape = Value
    vale TRIAGLE, SQUARE, RECTANGLE, HEXAGON, OCTOGON = Value
}
