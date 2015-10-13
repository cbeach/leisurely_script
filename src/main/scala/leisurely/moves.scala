package org.leisurelyscript

class LegalMove(val owner:Player, val piece:Piece, val precondition:() => Boolean, action:MoveAction, val postcondition:() => Boolean) {


}
