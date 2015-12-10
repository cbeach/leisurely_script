package org.leisurelyscript.gdl

case class BoardNode(
    val coord:Coordinate, 
    var equipment:List[Equipment]=List()) {
    var edges:List[BoardEdge]=List()
    var label:String = _

    def empty(truthFunction:(BoardNode)=>Boolean=null):Boolean = {
        if (truthFunction == null) {
            return equipment.length == 0
        } else {
            return truthFunction(this)
        }
    }

    def numberOfPieces(pieceCounter:(BoardNode)=>Int=null) = {
        if (pieceCounter == null) {
            equipment.size
        } else {
            pieceCounter(this)
        }
    }
}
