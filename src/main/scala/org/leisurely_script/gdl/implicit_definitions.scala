package org.leisurelyscript.gdl.ImplicitDefs

import org.apache.commons.codec.binary.Base64

import org.leisurelyscript.gdl.PieceRule
import org.leisurelyscript.gdl.{Player => PlayerClass, Coordinate, EndCondition, PlayerListWrapper,
    PieceRuleListWrapper, EndConditionListWrapper, Move, Game, PlayerValidator}
import spray.json._


package TypeClasses {

import java.io.{ObjectInputStream, ByteArrayInputStream, ObjectOutputStream, ByteArrayOutputStream}

import org.leisurely_script.gdl.AllPlayers
import org.leisurelyscript.gdl.{SubsetOfPlayers, SomePlayers, ConcretelyKnownPlayer, BoardNode}

    object LeisurelyScriptJSONProtocol extends DefaultJsonProtocol {
        sealed case class ConditionWrapper(func:(Game, Move)=>Boolean) {}
        //sealed case class AllPlayersWrapper(all:AllPlayers) {}
        implicit object legalMoveConditionFormatter extends RootJsonFormat[(Game, Move)=>Boolean] {
            def write(func:(Game, Move)=>Boolean):JsValue = {
                val byteStream = new ByteArrayOutputStream()
                new ObjectOutputStream(byteStream).writeObject(ConditionWrapper(func))
                val base64String:String = Base64.encodeBase64String(byteStream.toByteArray)
                JsObject(("condition", JsString(base64String)))
            }
            def read(json:JsValue):(Game, Move)=>Boolean = json match {
                case JsObject(fields) if fields.isDefinedAt("condition") =>
                    val byteArray = Base64.decodeBase64(fields("condition").convertTo[String])
                    val inputStream = new ByteArrayInputStream(byteArray)
                    new ObjectInputStream(inputStream).readObject() match {
                        case condition:ConditionWrapper => condition.func
                        case _ => deserializationError("not a (Game, Move) => Boolean")
                    }
                case _ => deserializationError("not a (Game, Move) => Boolean")
            }
        }
        implicit object PlayerFormatter extends RootJsonFormat[PlayerClass] {
            def write(player:PlayerClass):JsValue = {
                JsObject(("_type", JsString("Player")), ("name", JsString("name")))
            }
            def read(json:JsValue):PlayerClass = json match {
                case JsObject(fields) if fields.isDefinedAt("_type")
                  && fields("_type").convertTo[String] == "Player" =>
                    new PlayerClass(fields("name").convertTo[String])
                case _ => deserializationError("Not a Player.")
            }
        }
        implicit object ConcretelyKnownFormatter extends RootJsonFormat[ConcretelyKnownPlayer] {
            def write(cKP:ConcretelyKnownPlayer):JsValue = cKP match {
                case p:PlayerClass => p.toJson
            }
            def read(json:JsValue):ConcretelyKnownPlayer = json match {
                case JsObject(fields) if fields.isDefinedAt("_type") && fields("_type") == "Player" =>
                    json.convertTo[PlayerClass]
                case _ => deserializationError("Not a concretely known player.")
            }
        }
        implicit object PlayerValidatorFormatter extends RootJsonFormat[PlayerValidator] {
            def write(cKP:PlayerValidator):JsValue = cKP match {
                case p:PlayerClass => p.toJson
                case p:AllPlayers => p.toJson
                //case p:AnyPlayer => p.toJson
                //case p:PreviousPlayer => p.toJson
                //case p:CurrentPlayer => p.toJson
                //case p:NextPlayer => p.toJson
                //case p:NoPlayer => p.toJson
                //case p:SomePlayers => p.toJson
                //case p:SubsetOfPlayers => p.toJson
            }
            def read(json:JsValue):PlayerValidator = json match {
                case JsObject(fields) if fields.isDefinedAt("_type") && fields("_type") == "Player" =>
                    json.convertTo[PlayerClass]
                case _ => deserializationError("not a player validator")
            }
        }
        //implicit val PieceRuleFormatter = jsonFormat(PieceRule, "name", "owner", "legalMoves")
        implicit val coordinateFormatter = jsonFormat(Coordinate, "x", "y")
        implicit object BoardNodeFormatter extends JsonFormat[BoardNode] {
            def write(node:BoardNode):JsValue = {
                JsObject(("coord", node.coord.toJson))
            }
            def read(v:JsValue):BoardNode = {
                v.convertTo[BoardNode]
            }
        }
    }
}
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
