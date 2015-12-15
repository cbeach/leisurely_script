package org.leisurelyscript.gdl


case class Players(val players:List[Player]) {
  if (players.size != players.toSet.size) {
    throw new IllegalPlayerException("Duplicate players are not allowed")
  }

  def this(players:Player*) = {
    this(players.toList)
  }

  def this(players:List[Player], currentPlayer:Int) {
    this(players)
    this.currentPlayer = currentPlayer
  }

  var currentPlayer:Int = 0
  def all:List[Player] = players

  def previous:Player = {
    if (currentPlayer != 0) {

      players(currentPlayer - 1)
    } else {
      players(players.length - 1)
    }
  }

  def current:Player = players(currentPlayer)

  def next:Player = {
    if (currentPlayer <= players.length - 1) {
      players(currentPlayer + 1)
    } else {
      players(0)
    }
  }

  def endTurn:Players = {
    val currPlayer =
      if (currentPlayer < players.length - 1) {
        currentPlayer + 1
      } else {
        0
      }
    new Players(players, currPlayer)
  }

  def +(that:List[Player]):Players = {
    new Players(this.players ::: that)
  }
}
