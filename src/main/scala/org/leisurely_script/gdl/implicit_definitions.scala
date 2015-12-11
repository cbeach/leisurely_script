package org.leisurelyscript.gdl.ImplicitDefs

import org.apache.commons.codec.binary.Base64

import org.leisurelyscript.gdl.PieceRule
import org.leisurelyscript.gdl.{Player => PlayerClass, EndCondition, PlayerListWrapper,
    PieceRuleListWrapper, EndConditionListWrapper}
import spray.json._


package TypeClasses {
    import java.io.{ObjectInputStream, ByteArrayInputStream, ObjectOutputStream, ByteArrayOutputStream}
    import org.leisurelyscript.gdl._
    object LeisurelyScriptJSONProtocol extends DefaultJsonProtocol {
        private case class LegalMoveConditionWrapper(func:(Game, Move)=>Boolean) {}
        private case class EndConditionFunctionWrapper(func:(Game, PlayerClass)=>Boolean) {}
        implicit object legalMoveConditionFormatter extends RootJsonFormat[(Game, Move)=>Boolean] {
            def write(func:(Game, Move)=>Boolean):JsValue = {
                val byteStream = new ByteArrayOutputStream()
                new ObjectOutputStream(byteStream).writeObject(LegalMoveConditionWrapper(func))
                val base64String:String = Base64.encodeBase64String(byteStream.toByteArray)
                JsObject(("condition", JsString(base64String)))
            }
            def read(json:JsValue):(Game, Move)=>Boolean = json match {
                case JsObject(fields) if fields.isDefinedAt("condition") =>
                    val byteArray = Base64.decodeBase64(fields("condition").convertTo[String])
                    val inputStream = new ByteArrayInputStream(byteArray)
                    new ObjectInputStream(inputStream).readObject() match {
                        case condition:LegalMoveConditionWrapper => condition.func
                        case _ => deserializationError("not a (Game, Move) => Boolean")
                    }
                case _ => deserializationError("not a (Game, Move) => Boolean")
            }
        }
        implicit object EndConditionFunctionFormatter extends RootJsonFormat[(Game, PlayerClass)=>Boolean] {
            def write(func:(Game, PlayerClass)=>Boolean):JsValue = {
                val byteStream = new ByteArrayOutputStream()
                new ObjectOutputStream(byteStream).writeObject(EndConditionFunctionWrapper(func))
                val base64String:String = Base64.encodeBase64String(byteStream.toByteArray)
                JsObject(("condition", JsString(base64String)))
            }
            def read(json:JsValue):(Game, PlayerClass)=>Boolean = json match {
                case JsObject(fields) if fields.isDefinedAt("condition") =>
                    val byteArray = Base64.decodeBase64(fields("condition").convertTo[String])
                    val inputStream = new ByteArrayInputStream(byteArray)
                    new ObjectInputStream(inputStream).readObject() match {
                        case condition:EndConditionFunctionWrapper => condition.func
                        case _ => deserializationError("not a (Game, Move) => Boolean")
                    }
                case _ => deserializationError("not a (Game, Move) => Boolean")
            }
        }

        implicit object PlayerFormatter extends RootJsonFormat[PlayerClass] {
            def write(player:PlayerClass):JsValue = {
                JsObject(("_type", JsString("Player")), ("name", JsString(player.name)))
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
                case NoPlayer => JsObject(("_type", JsString("NoPlayer")))
                case p:PlayerClass => p.toJson
                case p:SomePlayers => p.toJson
                case p:SubsetOfPlayers => p.toJson
            }
            def read(json:JsValue):ConcretelyKnownPlayer = {
                json.asJsObject.fields("_type") match {
                    case JsString("NoPlayer") => NoPlayer
                    case JsString("Player") => json.convertTo[PlayerClass]
                    case JsString("SomePlayers") => json.convertTo[SomePlayers]
                    case JsString("SubsetOfPlayers") => json.convertTo[SubsetOfPlayers]
                    case err => deserializationError("not a PlayerValidator")
                }
            }
        }
        implicit object SomePlayersFormatter extends RootJsonFormat[SomePlayers] {
            def write(somePlayers:SomePlayers):JsValue = {
                JsObject(("_type", JsString("SomePlayers")),
                    ("players", somePlayers.getPlayers.toJson))
            }
            def read(json:JsValue):SomePlayers = {
                json match {
                    case JsObject(fields) if fields.isDefinedAt("_type") && fields.isDefinedAt("players")
                      && fields("_type").convertTo[String] == "SomePlayers" =>
                        new SomePlayers(fields("players").convertTo[Set[Player]])
                    case _ => deserializationError("not type SomePlayers")
                }
            }
        }
        implicit object SubsetOfPlayersFormatter extends RootJsonFormat[SubsetOfPlayers] {
            def write(subsetOfPlayers:SubsetOfPlayers):JsValue = {
                JsObject(("_type", JsString("SubsetOfPlayers")),
                    ("players", JsArray(subsetOfPlayers.getPlayers.toVector.map(_.toJson))))
            }
            def read(json:JsValue):SubsetOfPlayers = {
                json match {
                    case JsObject(fields) if fields.isDefinedAt("_type") && fields.isDefinedAt("players")
                      && fields("_type").convertTo[String] == "SubsetOfPlayers" =>
                        new SubsetOfPlayers(fields("players").convertTo[Set[Player]])
                    case _ => deserializationError("not type SubsetOfPlayers")
                }
            }
        }
        implicit object PlayerValidatorFormatter extends RootJsonFormat[PlayerValidator] {
            def write(playerValidator:PlayerValidator):JsValue = playerValidator match {
                case AllPlayers => JsObject(("_type", JsString("AllPlayers")))
                case AnyPlayer => JsObject(("_type", JsString("AnyPlayer")))
                case PreviousPlayer => JsObject(("_type", JsString("PreviousPlayer")))
                case CurrentPlayer => JsObject(("_type", JsString("CurrentPlayer")))
                case NextPlayer => JsObject(("_type", JsString("NextPlayer")))
                case NoPlayer => JsObject(("_type", JsString("NoPlayer")))
                case p:PlayerClass => p.toJson
                case p:SomePlayers => p.toJson
                case p:SubsetOfPlayers => p.toJson
            }
            def read(json:JsValue):PlayerValidator = {
                json.asJsObject.fields("_type") match {
                    case JsString("AllPlayers") => AllPlayers
                    case JsString("AnyPlayer") => AnyPlayer
                    case JsString("PreviousPlayer") => PreviousPlayer
                    case JsString("CurrentPlayer") => CurrentPlayer
                    case JsString("NextPlayer") => NextPlayer
                    case JsString("NoPlayer") => NoPlayer
                    case JsString("Player") => json.convertTo[PlayerClass]
                    case JsString("SomePlayers") => json.convertTo[SomePlayers]
                    case JsString("SubsetOfPlayers") => json.convertTo[SubsetOfPlayers]
                    case err => deserializationError("not a PlayerValidator")
                }
            }
        }
        //implicit val PieceRuleFormatter = jsonFormat(PieceRule, "name", "owner", "legalMoves")

        implicit val coordinateFormatter = jsonFormat(Coordinate, "x", "y")
        implicit object BoardNodeFormatter extends JsonFormat[BoardNode] {
            def write(node:BoardNode):JsValue = {
                JsObject(("coord", node.coord.toJson))
            }
            def read(json:JsValue):BoardNode = {
                json match {
                    case JsObject(fields) => {
                        BoardNode(fields("coord").convertTo[Coordinate])
                    }
                    case _ => deserializationError("Not a BoardNode")
                }
            }
        }
        implicit object BoardEdgeFormatter extends JsonFormat[BoardEdge] {
            def write(edge:BoardEdge):JsValue = {
                JsObject(("source", edge.nodes._1.toJson), ("dest", edge.nodes._2.toJson),
                    ("direction", JsString(edge.direction.toString)))
            }
            def read(json:JsValue):BoardEdge = {
                val fields = json.asJsObject.fields
                new BoardEdge((fields("source").convertTo[BoardNode], fields("dest").convertTo[BoardNode]),
                    Direction.withName(fields("direction").convertTo[String]))

            }
        }
        implicit object GraphFormatter extends JsonFormat[Graph] {
            def write(graph:Graph):JsValue = {
                JsObject(("nodes", graph.nodes.map(_._2).toJson), ("edges", graph.edges.toJson))
            }
            def read(json:JsValue):Graph = {
                val fields = json.asJsObject.fields
                val nodes:Map[Coordinate, BoardNode] =
                    fields("nodes").convertTo[List[BoardNode]].map(node => {
                    (node.coord, node)
                }).toMap
                new Graph(nodes, fields("edges").convertTo[List[BoardEdge]])
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
