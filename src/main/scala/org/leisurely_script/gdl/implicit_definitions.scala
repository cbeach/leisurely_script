package org.leisurely_script.gdl.ImplicitDefs

import scala.reflect._
import scala.reflect.runtime.universe._
import org.apache.commons.codec.binary.Base64

import org.leisurely_script.gdl.PieceRule
import org.leisurely_script.gdl.{Player => PlayerClass, EndCondition, PlayerListWrapper,
  PieceRuleListWrapper, EndConditionListWrapper}
import org.leisurely_script.gdl.expressions._
import spray.json._


package TypeClasses {
import java.io.{ObjectInputStream, ByteArrayInputStream, ObjectOutputStream, ByteArrayOutputStream}
import org.leisurely_script.gdl._
import org.leisurely_script.gdl.types._
import org.leisurely_script.implementation.Game

import scala.collection.mutable

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
        JsObject(("nodes", graph.nodes.toList.toJson), ("edges", graph.edges.toList.toJson))
      }
      def read(json:JsValue):Graph = {
        val fields = json.asJsObject.fields
        new Graph(fields("nodes").convertTo[List[BoardNode]],
          fields("edges").convertTo[List[BoardEdge]])
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
    implicit object BoardFormatter extends JsonFormat[BoardRuleSet] {
      def write(board:BoardRuleSet):JsValue = {
        JsObject(("size", board.size.toJson),
          ("boardShape", board.boardShape.toJson),
          ("neighborType", board.neighborType.toJson),
          ("nodeShape", board.nodeShape.toJson),
          ("pieces", board.pieces.toJson),
          ("graph", board.graph.toJson))
      }
      def read(json:JsValue):BoardRuleSet = {
        json match {
          case JsObject(fields) => new BoardRuleSet(fields("size").convertTo[List[Int]],
            fields("boardShape").convertTo[Shape.Value],
            fields("neighborType").convertTo[NeighborType.Value],
            fields("nodeShape").convertTo[Shape.Value],
            fields("pieces").convertTo[List[PieceRule]],
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
    implicit object NInARowExpressionFormatter extends JsonFormat[NInARowExpression] {
      def write(expr:NInARowExpression):JsValue = {
        //pieceRule:PieceRule, boardRuleSet:BoardRuleSet, player:PlayerValidator, neighborType:NeighborType=null
        JsObject(
          ("n", expr.n.toJson),
          ("pieceRule", expr.pieceRule.toJson),
          ("boardRuleSet", expr.boardRuleSet.toJson),
          ("player", expr.player.toJson),
          ("neighborType", expr.neighborType.toJson)
        )
      }
      def read(json:JsValue):NInARowExpression = {
        json match {
          case JsObject(fields) => NInARowExpression(
            fields("n").convertTo[Int],
            fields("pieceRule").convertTo[PieceRule],
            fields("boardRuleSet").convertTo[BoardRuleSet],
            fields("player").convertTo[PlayerValidator],
            fields("neighborType").convertTo[NeighborType.Value]
          )
        }
      }
    }
    implicit object BoardFullExpressionFormatter extends JsonFormat[BoardFullExpression] {
      def write(boardFull:BoardFullExpression):JsValue = {
        JsObject(("boardRuleSet", boardFull.boardRuleSet.toJson))
      }
      def read(json:JsValue):BoardFullExpression = {
        json match {
          case JsObject(fields) => BoardFullExpression(fields("boardRuleSet").convertTo[BoardRuleSet])
        }
      }
    }
    implicit object BoardEmptyExpressionFormatter extends JsonFormat[BoardEmptyExpression] {
      def write(boardEmpty:BoardEmptyExpression):JsValue = {
        JsObject(("boardRuleSet", boardEmpty.boardRuleSet.toJson))
      }
      def read(json:JsValue):BoardEmptyExpression = {
        json match {
          case JsObject(fields) => BoardEmptyExpression(fields("boardRuleSet").convertTo[BoardRuleSet])
        }
      }
    }
    implicit object GameExpressionFormatter extends JsonFormat[GameExpression[_]] {
      def write(expr:GameExpression):JsValue = {
        expr match {
          case e:BooleanExpression => e.toJson
          case e:ByteExpression => e.toJson
          case e:CharExpression => e.toJson
          case e:DoubleExpression => e.toJson
          case e:FloatExpression => e.toJson
          case e:IntExpression => e.toJson
          case e:LongExpression => e.toJson
          case e:ShortExpression => e.toJson
        }
      }
      def read(json:JsValue): GameExpression[_] = {
        json match {
          case JsObject(fields) => fields("type").convertTo[String] match {
            case "Boolean" => BooleanExpression(fields("value").convertTo[Boolean])
            case "Byte" => ByteExpression(fields("value").convertTo[Byte])
            case "Char" => CharExpression(fields("value").convertTo[Char])
            case "Double" => DoubleExpression(fields("value").convertTo[Double])
            case "Float" => FloatExpression(fields("value").convertTo[Float])
            case "Int" => IntExpression(fields("value").convertTo[Int])
            case "Long" => LongExpression(fields("value").convertTo[Long])
            case "Short" => ShortExpression(fields("value").convertTo[Short])
          }
        }
      }
    }
    implicit object BooleanExpressionFormatter extends JsonFormat[BooleanExpression] {
      def write(expr:BooleanExpression):JsValue = {
        JsObject(
          ("type", "Boolean".toJson),
          ("value", expr.toJson)
        )
      }
      def read(json:JsValue):BooleanExpression = {
        json match {
          case JsObject(fields) => BooleanExpression(fields("value").convertTo[Boolean])
        }
      }
    }
    implicit object ByteExpressionFormatter extends JsonFormat[ByteExpression] {
      def write(expr:ByteExpression):JsValue = {
        JsObject(
          ("type", "Boolean".toJson),
          ("value", expr.toJson)
        )
      }
      def read(json:JsValue):ByteExpression = {
        json match {
          case JsObject(fields) => ByteExpression(fields("value").convertTo[Byte])
        }
      }
    }
    implicit object CharExpressionFormatter extends JsonFormat[CharExpression] {
      def write(expr:CharExpression):JsValue = {
        JsObject(
          ("type", "Char".toJson),
          ("value", expr.toJson)
        )
      }
      def read(json:JsValue):CharExpression = {
        json match {
          case JsObject(fields) => CharExpression(fields("value").convertTo[Char])
        }
      }
    }
    implicit object DoubleExpressionFormatter extends JsonFormat[DoubleExpression] {
      def write(expr:DoubleExpression):JsValue = {
        JsObject(
          ("type", "Double".toJson),
          ("value", expr.toJson)
        )
      }
      def read(json:JsValue):DoubleExpression = {
        json match {
          case JsObject(fields) => DoubleExpression(fields("value").convertTo[Double])
        }
      }
    }
    implicit object FloatExpressionFormatter extends JsonFormat[FloatExpression] {
      def write(expr:FloatExpression):JsValue = {
        JsObject(
          ("type", "Float".toJson),
          ("value", expr.toJson)
        )
      }
      def read(json:JsValue):FloatExpression = {
        json match {
          case JsObject(fields) => FloatExpression(fields("value").convertTo[Float])
        }
      }
    }
    implicit object IntExpressionFormatter extends JsonFormat[IntExpression] {
      def write(expr:IntExpression):JsValue = {
        JsObject(
          ("type", "Int".toJson),
          ("value", expr.toJson)
        )
      }
      def read(json:JsValue):IntExpression = {
        json match {
          case JsObject(fields) => IntExpression(fields("value").convertTo[Int])
        }
      }
    }
    implicit object LongExpressionFormatter extends JsonFormat[LongExpression] {
      def write(expr:LongExpression):JsValue = {
        JsObject(
          ("type", "Long".toJson),
          ("value", expr.toJson)
        )
      }
      def read(json:JsValue):LongExpression = {
        json match {
          case JsObject(fields) => LongExpression(fields("value").convertTo[Long])
        }
      }
    }
    implicit object ShortExpressionFormatter extends JsonFormat[ShortExpression] {
      def write(expr:ShortExpression):JsValue = {
        JsObject(
          ("type", "Short".toJson),
          ("value", expr.toJson)
        )
      }
      def read(json:JsValue):ShortExpression = {
        json match {
          case JsObject(fields) => ShortExpression(fields("value").convertTo[Short])
        }
      }
    }
    implicit object conditionalFormatter extends JsonFormat[ConditionalExpression[_]] {
      def write(value:ConditionalExpression[_]):JsValue = {
        JsObject(
          ("condition", value.conditionExpr.toJson),
          ("then", value.thenExpr.toJson),
          ("otherwise", value.otherwise.toJson)
        )
      }
      def read(json:JsValue):ConditionalExpression[_] = {
        json match {
          case JsObject(fields) => {
            new ConditionalExpression(
              fields("condition").convertTo[BooleanExpression],
              fields("then").convertTo[GameExpression[_]],
              fields("otherwise").convertTo[Option[GameExpression[_]]]
            )
          }
        }

      }
    }
    implicit object endConditionFormatter extends JsonFormat[EndCondition] {
      def write(endCondition: EndCondition):JsValue = {
        JsObject(("expr", endCondition.result.toJson))
      }
      def read(json: JsValue):EndCondition = {
        json match {
          case JsObject(fields) => EndCondition(fields("expr").convertTo[GameExpression[SGameResult]])
        }
      }
    }
    implicit val SGameResultFormatter = new JsonFormat[SGameResult] {
      def write(value:SGameResult):JsValue = {
        JsObject(
          ("players", value.players.toJson),
          ("result", value.result.toJson))
      }
      def read(json:JsValue):SGameResult = {
        json match {
          case JsObject(fields) => SGameResult(
            fields("players").convertTo[Set[Player]],
            fields("result").convertTo[GameResultState.Value])
        }
      }
    }
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
    implicit object TypeTagFormatter extends JsonFormat[TypeTag[_]] {
      def write(value:TypeTag[_]):JsValue = {
        val byteStream = new ByteArrayOutputStream()
        new ObjectOutputStream(byteStream).writeObject(value)
        val base64String:String = Base64.encodeBase64String(byteStream.toByteArray)
        JsObject(("type", JsString(base64String)))
      }
      def read(json:JsValue):TypeTag[_] = json match {
        case JsObject(fields) if fields.isDefinedAt("type") =>
          val byteArray = Base64.decodeBase64(fields("type").convertTo[String])
          val inputStream = new ByteArrayInputStream(byteArray)
          new ObjectInputStream(inputStream).readObject() match {
            case tt:TypeTag[_] => tt
            case thing => deserializationError(s"got $thing, a TypeTag")
          }
        case thing => deserializationError(s"got $thing, a TypeTag")
      }
    }
    implicit object ClassTagFormatter extends JsonFormat[ClassTag[_]] {
      def write(value:ClassTag[_]):JsValue = {
        val byteStream = new ByteArrayOutputStream()
        new ObjectOutputStream(byteStream).writeObject(value)
        val base64String:String = Base64.encodeBase64String(byteStream.toByteArray)
        JsObject(("class", JsString(base64String)))
      }
      def read(json:JsValue):ClassTag[_] = json match {
        case JsObject(fields) if fields.isDefinedAt("class") =>
          val byteArray = Base64.decodeBase64(fields("class").convertTo[String])
          val inputStream = new ByteArrayInputStream(byteArray)
          new ObjectInputStream(inputStream).readObject() match {
            case tt:ClassTag[_] => tt
            case thing => deserializationError(s"got $thing, a ClassTag")
          }
        case thing => deserializationError(s"got $thing, a ClassTag")
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
    implicit val gameResultFormatter = jsonFormat2(SGameResult)
    implicit object GameFormatter extends RootJsonFormat[GameRuleSet] {
      def write(game:GameRuleSet):JsValue = {
        JsObject(
          ("name", game.name.toJson),
          ("players", game.players.toJson),
          ("board", game.board.toJson),
          ("pieces", game.pieces.toJson),
          ("endConditions", game.endConditions.toJson),
          ("scoringFunction", game.playerScoringFunction.toJson)
        )
      }
      def read(json:JsValue):GameRuleSet = json match {
        case JsObject(fields) => new GameRuleSet(
          fields("name").convertTo[String],
          fields("players").convertTo[Players],
          fields("board").convertTo[BoardRuleSet],
          fields("pieces").convertTo[List[PieceRule]],
          fields("endConditions").convertTo[List[EndCondition]],
          Some(fields("scoringFunction").convertTo[(Player, GameResultState.Value, Option[Player])=>Double])
        )
        case thing => deserializationError(s"Expected Game, got $thing")
      }
    }
  }
}
package Views {

import org.leisurely_script.gdl.SpecificPlayer
import org.leisurely_script.gdl.expressions.WellFormedConditionalExpressionBuilder
import org.leisurely_script.gdl.types._

  object Game {
    implicit def playerListToPlayerListWrapper(
      players:List[PlayerClass]):PlayerListWrapper = PlayerListWrapper(players)
    implicit def pieceRuleListToPieceRuleListWrapper(
      pieces:List[PieceRule]):PieceRuleListWrapper = PieceRuleListWrapper(pieces)
    implicit def endConditionListToEndConditionListWrapper(
      endConditions:List[EndCondition]):EndConditionListWrapper = EndConditionListWrapper(endConditions)
    implicit def playerToSpecificPlayer(player:PlayerClass):SpecificPlayer = SpecificPlayer(player)
  }
  object Player {
    implicit def playerToPlayerSet(player:PlayerClass):Set[PlayerClass] = Set(player)
  }
  object TypeConversions {
    implicit def BooleanToBooleanExpression(value:Boolean):BooleanExpression = BooleanExpression(value)
    implicit def ByteToByteExpression(value:Byte):ByteExpression = ByteExpression(value)
    implicit def CharToCharExpression(value:Char):CharExpression = CharExpression(value)
    implicit def DoubleToDoubleExpression(value:Double):DoubleExpression = DoubleExpression(value)
    implicit def FloatToFloatExpression(value:Float):FloatExpression = FloatExpression(value)
    implicit def IntToIntExpression(value:Int):IntExpression = IntExpression(value)
    implicit def LongToLongExpression(value:Long):LongExpression = LongExpression(value)
    implicit def ShortToShortExpression(value:Short):ShortExpression = ShortExpression(value)

    implicit def BooleanExpressionToBoolean(expr:BooleanExpression):Boolean = expr.value
    implicit def ByteExpressionToByte(expr:ByteExpression):Byte = expr.value
    implicit def CharExpressionToChar(expr:CharExpression):Char = expr.value
    implicit def DoubleExpressionToDouble(expr:DoubleExpression):Double = expr.value
    implicit def FloatExpressionToFloat(expr:FloatExpression):Float = expr.value
    implicit def IntExpressionToInt(expr:IntExpression):Int = expr.value
    implicit def LongExpressionToLong(expr:LongExpression):Long = expr.value
    implicit def ShortExpressionToShort(expr:ShortExpression):Short = expr.value

    implicit def WellFormedConditionalBuilderToConditional[T]
      (cBuilder:WellFormedConditionalExpressionBuilder[TRUE, TRUE, T]):ConditionalExpression[T] =
      cBuilder.build
    implicit def FinalizedConditionalBuilderToConditional[T]
      (cBuilder:FinalizedConditionalExpressionBuilder[TRUE, TRUE, T]):ConditionalExpression[T] =
      cBuilder.build
  }
}
