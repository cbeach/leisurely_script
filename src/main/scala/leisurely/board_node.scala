package org.leisurelyscript

case class BoardNode(coord:Coordinate, 
    var edges:List[BoardEdge]=List(),
    var equipment:List[Equipment]=List()) {

    def empty(truthFunction:(BoardNode)=>Boolean=null):Boolean = {
        if (truthFunction == null) {
            return equipment.length == 0
        } else {
            return truthFunction(this)
        }
    }
}
