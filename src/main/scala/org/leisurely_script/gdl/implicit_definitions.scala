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
    implicit object PlayerFormatter extends RootJsonFormat[PlayerClass] {
      def write(player:PlayerClass):JsValue = {
        JsObject(("_type", JsString("Player")), ("name", JsString(player.name)))
      }
      def read(json:JsValue):PlayerClass = json match {
        case JsObject(fields) if fields.isDefinedAt("_type")
          && fields("_type").convertTo[String] == "Player" =>
          new PlayerClass(fields("name").convertTo[String])
        case thing => deserializationError(s"got $thing, expected Player")
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
          case thing => deserializationError(s"got $thing, expected SomePlayers")
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
          case thing => deserializationError(s"got $thing, expected SubsetOfPlayers")
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
    implicit object PlayersFormatter extends RootJsonFormat[Players] {
      def write(players:Players):JsValue = {
        JsObject(("players", players.all.toJson))
      }
      def read(json:JsValue):Players = {
        json match {
          case JsObject(fields) => new Players(fields("players").convertTo[List[Player]])
          case thing => deserializationError(s"Expected Players, got $thing")
        }
      }
    }

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
          case thing => deserializationError(s"got $thing, expected BoardNode")
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
    implicit val shapeFormatter = new JsonFormat[Shape.Value] {
      def write(shape:Shape.Value):JsValue = JsString(shape.toString)
      def read(json:JsValue):Shape.Value =
        Shape.withName(json.convertTo[String])
    }
    implicit val neighborTypeFormatter = new JsonFormat[NeighborType.Value] {
      def write(neighborType:NeighborType.Value):JsValue = JsString(neighborType.toString)
      def read(json:JsValue):NeighborType.Value =
        NeighborType.withName(json.convertTo[String])
    }
    implicit object BoardFormatter extends JsonFormat[Board] {
      def write(board:Board):JsValue = {
        JsObject(("size", board.size.toJson),
          ("boardShape", board.boardShape.toJson),
          ("neighborType", board.neighborType.toJson),
          ("nodeShape", board.nodeShape.toJson),
          ("graph", board.graph.toJson))
      }
      def read(json:JsValue):Board = {
        json match {
          case JsObject(fields) => new Board(fields("size").convertTo[List[Int]],
            fields("boardShape").convertTo[Shape.Value],
            fields("neighborType").convertTo[NeighborType.Value],
            fields("nodeShape").convertTo[Shape.Value],
            fields("graph").convertTo[Graph])
          case thing => deserializationError(s"Expected Board, got $thing")
        }
      }
    }

    implicit val gameResultStateFormatter = new JsonFormat[GameResultState.Value] {
      def write(state:GameResultState.Value):JsValue = JsString(state.toString)
      def read(json:JsValue):GameResultState.Value =
        GameResultState.withName(json.convertTo[String])
    }
    private case class EndConditionFunctionWrapper(func:(Game, PlayerClass)=>Boolean) {}
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
            case thing => deserializationError(s"got $thing, expected (Game, Player) => Boolean")
          }
        case thing => deserializationError(s"got $thing, expected (Game, Player) => Boolean")
      }
    }
    implicit val EndConditionFormatter = jsonFormat(EndCondition, "result", "affectedPlayer", "condition")

    private case class LegalMoveConditionWrapper(func:(Game, Move)=>Boolean) {}
    implicit val moveActionFormatter = new JsonFormat[MoveAction.Value] {
      def write(action:MoveAction.Value):JsValue = JsString(action.toString)
      def read(json:JsValue):MoveAction.Value = MoveAction.withName(json.convertTo[String])
    }
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
            case thing => deserializationError(s"got $thing, expected (Game, Move) => Boolean")
          }
        case thing => deserializationError(s"got $thing, expected (Game, Move) => Boolean")
      }
    }
    implicit object LegalMoveFormatter extends RootJsonFormat[LegalMove] {
      def write(legalMove:LegalMove):JsValue = {
        JsObject(("owner", legalMove.owner.toJson),
          ("precondition", legalMove.precondition.toJson),
          ("action", legalMove.action.toJson),
          ("postcondition", legalMove.postcondition.toJson))
      }
      def read(json:JsValue):LegalMove = {
        json match {
          case JsObject(fields) => new LegalMove(fields("owner").convertTo[PlayerValidator],
            fields("precondition").convertTo[(Game, Move)=>Boolean],
            fields("action") match {
              case JsString(str) => MoveAction.withName(str)
              case thing => deserializationError(s"expected a MoveAction, got $thing")
            },
            fields("postcondition").convertTo[(Game, Move)=>Boolean])
          case thing => deserializationError(s"got $thing, expected LegalMove")
        }
      }
    }
    implicit val PieceRuleFormatter = new JsonFormat[PieceRule] {
      def write(pieceRule:PieceRule):JsValue = JsObject(("name", JsString(pieceRule.name)),
        ("owner", pieceRule.owner.toJson), ("legalMoves", pieceRule.legalMoves.toJson))
      def read(json:JsValue):PieceRule = json match {
        case JsObject(fields) => PieceRule(fields("name").convertTo[String],
          fields("owner").convertTo[PlayerValidator], fields("legalMoves").convertTo[List[LegalMove]])
        case thing => deserializationError(s"Expected PieceRule, got $thing")
      }
    }

    private case class ScoringFunctionWrapper(func:(Player, GameResultState.Value, Option[Player])=>Double) {}
    implicit object PlayerScoringFunctionFormatter
      extends RootJsonFormat[(Player, GameResultState.Value, Option[Player])=>Double] {
      def write(func:(Player, GameResultState.Value, Option[Player])=>Double):JsValue = {
        val byteStream = new ByteArrayOutputStream()
        new ObjectOutputStream(byteStream).writeObject(ScoringFunctionWrapper(func))
        val base64String:String = Base64.encodeBase64String(byteStream.toByteArray)
        JsObject(("condition", JsString(base64String)))
      }
      def read(json:JsValue):(Player, GameResultState.Value, Option[Player])=>Double = json match {
        case JsObject(fields) if fields.isDefinedAt("condition") =>
          val byteArray = Base64.decodeBase64(fields("condition").convertTo[String])
          val inputStream = new ByteArrayInputStream(byteArray)
          new ObjectInputStream(inputStream).readObject() match {
            case func:ScoringFunctionWrapper => func.func
            case thing => deserializationError(s"got $thing, expected (Player, GameResultState.Value, Option[Player])=>Double")
          }
        case thing => deserializationError(s"got $thing, expected (Player, GameResultState.Value, Option[Player])=>Double")
      }
    }
    implicit val gameStatusFormatter = new JsonFormat[GameStatus.Value] {
      def write(status:GameStatus.Value):JsValue = JsString(status.toString)
      def read(json:JsValue):GameStatus.Value = GameStatus.withName(json.convertTo[String])
    }
    implicit val gameResultFormatter = jsonFormat3(GameResult)
    implicit object GameFormatter extends RootJsonFormat[Game] {
      def write(game:Game):JsValue = {
        JsObject(
          ("name", game.name.toJson),
          ("players", game.players.toJson),
          ("board", game.board.toJson),
          ("pieces", game.pieces.toJson),
          ("endConditions", game.endConditions.toJson),
          ("gameResult", game.gameResult.toJson),
          ("status", game.status.toJson),
          ("scoringFunction", game.playerScoringFunction.toJson)
        )
      }
      def read(json:JsValue):Game = json match {
        case JsObject(fields) => new Game(
          fields("name").convertTo[String],
          fields("players").convertTo[Players],
          fields("board").convertTo[Board],
          fields("pieces").convertTo[List[PieceRule]],
          fields("endConditions").convertTo[List[EndCondition]],
          List[Game](),
          Some(fields("gameResult").convertTo[GameResult]),
          fields("status").convertTo[GameStatus.Value],
          Some(fields("scoringFunction").convertTo[(Player, GameResultState.Value, Option[Player])=>Double])
        )
        case thing => deserializationError(s"Expected Game, got $thing")
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
