package org.leisurelyscript


class Players(players:List[Player]) {
    def this(players:Player*) = {
        this(players.toList)
    }
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

    class Previous extends Player() {
        def apply(player:Player):Boolean = {
            player == previous 
        }
    }
    class Current extends Player() {
        def apply(player:Player):Boolean = {
            player == current 
        }
    }
    class Next extends Player() {
        def apply(player:Player):Boolean = {
            player == next 
        }
	}
    class SomePlayers(players:List[Player]) extends Player() {
        val validPlayers = { 
            players.sortWith(all.indexOf(_) < all.indexOf(_))
        }
        def apply(players:List[Player]):Boolean = {
            players.sortWith(validPlayers.indexOf(_) < validPlayers.indexOf(_)) == validPlayers
        }
	}
    class NoPlayer extends Player() {
        def apply(player:Player):Boolean = {
            false 
        }
	}
    class Any extends Player() {
        def apply(player:Player):Boolean = {
            true 
        }
	}
    class All extends Player() {
        def apply(players:List[Player]):Boolean = {
            players.sortWith(all.indexOf(_) < all.indexOf(_)) == all
        }
	}
}


