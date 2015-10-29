package org.leisurelyscript


private[leisurelyscript] class Players(players:List[Player]) {
    var currentPlayer:Int = 0
    def all:List[Player] = players

    def previous:Player = {
        if (currentPlayer != 0) players(currentPlayer - 1) 
            else players(players.length - 1)
    }

    def current:Player = players(currentPlayer)

    def next:Player = {
        if (currentPlayer <= players.length - 1) players(currentPlayer + 1) 
            else players(0)
    }

    def endTurn():Unit = {
        currentPlayer = if (currentPlayer <= players.length - 1) 
            currentPlayer + 1 else 0
    }
    
    def +(that:List[Player]):Players = {
        new Players(this.players ::: that)
    }
}


