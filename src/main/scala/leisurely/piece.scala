package org.leisurelyscript


class Piece(val name:String, val owner:Player, val legalMoves:List[LegalMove]) extends Equipment {
    def this(other:Piece) = {
        this(other.name, other.owner, other.legalMoves)
    }

    override def copy:Piece = {
        new Piece(name, owner, legalMoves)
    }

    def copy(newOwner:Player):Piece = {
        new Piece(name, newOwner, legalMoves)
    }

    def legalMoves(game:Game, player:Player): List[Move] = {
        var moveList:List[Move] = List()
        for (legalMove <- legalMoves) {
            for (node <- game.board.nodes) {
                val newMove = Move(this, player, legalMove.action, node._2)
                if (legalMove.legal(game, newMove)) {
                    moveList = newMove :: moveList
                }
            }
        }
        moveList
    }

    def isMoveLegal(game:Game, move:Move):Boolean = {
        for (legalMove <- legalMoves) {
            if (legalMove.legal(game, move)) {
                return true
            }
        }
        false
    }

    override def toString:String = {
        s"Piece(String(${name}), ${owner}, ${legalMoves})"
    }
    def wellFormed:Unit = {
        if (legalMoves.size == 0) {
            throw new IllegalPieceException("A piece must have at least one legal move. None found.")
        }
    }
}
