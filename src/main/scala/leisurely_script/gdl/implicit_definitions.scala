package org.leisurelyscript.ImplicitDefs

import org.leisurelyscript.gdl.{Player => PlayerClass, PlayerValidator, EndCondition, PieceRule,
    SpecificPlayer, PlayerListWrapper, PieceRuleListWrapper, EndConditionListWrapper}


package Views {
    object Game {
        implicit def playerListToPlayerListWrapper(
            players:List[PlayerClass]):PlayerListWrapper = PlayerListWrapper(players)
        implicit def pieceRuleListToPieceRuleListWrapper(
            pieces:List[PieceRule]):PieceRuleListWrapper = PieceRuleListWrapper(pieces)
        implicit def endConditionListToEndConditionListWrapper(
            endConditions:List[EndCondition]):EndConditionListWrapper = EndConditionListWrapper(endConditions)
    }

    object Player {
        implicit def playerToPlayerSet(player:PlayerClass):Set[PlayerClass] = Set(player)
    }
}
