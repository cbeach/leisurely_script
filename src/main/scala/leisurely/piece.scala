package org.leisurelyscript


class Piece(val name:String, val owner:Player, val moves:List[LegalMove]) extends Equipment {
    def legalMoves(game:Game, player:Player): List[Move] = {
        var moveList:List[Move] = List()
        for (move <- moves) {
            for (node <- game.board.nodes) {
                val newMove = Move(this, player, move.action, node._2)
                if (move.legal(game, player, newMove)) {
                    moveList = newMove :: moveList
                }
            }
        }
        moveList
    }
}