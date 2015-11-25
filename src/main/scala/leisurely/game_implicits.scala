package org.leisurelyscript.ImplicitDefs

import org.leisurelyscript.{Player => PlayerClass, PlayerValidator, SpecificPlayer}


object Game {
    
}

object Player {
    implicit def playerToPlayerSet(player:PlayerClass):Set[PlayerClass] = Set(player)
}
