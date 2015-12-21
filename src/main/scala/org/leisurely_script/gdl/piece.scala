package org.leisurely_script.gdl


case class PhysicalPiece(val name:String, val owner:ConcretelyKnownPlayer) extends Equipment {
  override def copy:Equipment = {
    PhysicalPiece(name, owner)
  }
}

case class PieceRule(val name:String, val owner:PlayerValidator, val legalMoves:List[LegalMove]) {
  def getPhysicalPiece(player:ConcretelyKnownPlayer):PhysicalPiece = {
    PhysicalPiece(name, player)
  }

  def legalMoves(game:Game, player:Player): List[Move] = {
    var moveList:List[Move] = List()
    for (legalMove <- legalMoves) {
      for (node <- game.board.nodes) {
        val newMove = Move(PhysicalPiece(name, player), player, legalMove.action, node._2)
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
    s"PieceRule(String(${name}), ${owner}, ${legalMoves})"
  }

  def wellFormed:Unit = {
    if (legalMoves.size == 0) {
      throw new IllegalPieceException("A piece must have at least one legal move. None found.")
    }
  }
}
