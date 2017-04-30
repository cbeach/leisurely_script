package org.beachc.leisurely.metaprogrammers.tokens

import scala.meta._
import org.beachc.leisurely.metaprogrammers.Utils._

object Implicits {
  implicit def Int2IntLit(i: Int) = new INTEGER_LIT(i) 
  implicit def String2StringLit(s: String) = new STRING_LIT(s) 
}
object TypesAliases {
  type LEGAL_MOVE_PARAM_TYPE = (PLAYER_DEFN, STRING_LIT, LM_FUNCTION_DEFN, ACTION_DEFN)
}

import Implicits._
import TypesAliases._

trait TOKEN[T] {
  val value: T
  override def toString = s"${this.getClass.getName.split('.').toList.last}( ${value} )"
}

trait PART_DEFN {}

class NAME(val value: String) extends TOKEN[String] {}
class GAME_ID(val value: NAME) extends TOKEN[NAME] {}
class GAME_DEFN(val value: (GAME_ID, MEMBERS)) extends TOKEN[(GAME_ID, MEMBERS)] {}
class INTEGER_LIT(val value: Int) extends TOKEN[Int] {}
class FLOATING_POINT_LIT(val value: Float) extends TOKEN[Float] {}
class BOOLEAN_LIT(val value: Boolean) extends TOKEN[Boolean] {}
class CHAR_LIT(val value: Char) extends TOKEN[Char] {}
class STRING_LIT(val value: String) extends TOKEN[String] {}
class MEMBERS(val value: Map[PART_NAME, PART_DEFN]) extends TOKEN[Map[PART_NAME, PART_DEFN]] {}

class PART_NAME(val value: String) extends TOKEN[String] {}
class PLAYER_LIST_DEFN(val value: List[PLAYER_DEFN]) extends TOKEN[List[PLAYER_DEFN]] with PART_DEFN {}
class PLAYER_DEFN(val value: STRING_LIT) extends TOKEN[STRING_LIT] {}

class GRAPH_DEFN(val value: (List[NODE_DEFN], List[EDGE_DEFN])) extends TOKEN[(List[NODE_DEFN], List[EDGE_DEFN])] with PART_DEFN {}
class NODE_DEFN(val value: (INTEGER_LIT, INTEGER_LIT)) extends TOKEN[(INTEGER_LIT, INTEGER_LIT)] {}
class EDGE_DEFN(val value: (NODE_DEFN, NODE_DEFN)) extends TOKEN[(NODE_DEFN, NODE_DEFN)] {}

class PIECE_LIST_DEFN(val value: List[PIECE_DEFN]) extends TOKEN[List[PIECE_DEFN]] with PART_DEFN {}
class PIECE_DEFN(val value: (STRING_LIT, PLAYER_DEFN)) extends TOKEN[(STRING_LIT, PLAYER_DEFN)] {}

class LEGALMOVE_LIST_DEFN(val value: List[LEGALMOVE_DEFN]) extends TOKEN[List[LEGALMOVE_DEFN]] with PART_DEFN {}
class LEGALMOVE_DEFN(val value: LEGAL_MOVE_PARAM_TYPE) extends TOKEN[LEGAL_MOVE_PARAM_TYPE] {}
class LM_FUNCTION_DEFN(val value: meta.Tree) extends TOKEN[meta.Tree] {}

class ENDCONDITION_LIST_DEFN(val value: List[ENDCONDITION_DEFN]) extends TOKEN[List[ENDCONDITION_DEFN]] with PART_DEFN {}
class ENDCONDITION_DEFN(val value: (PLAYER_DEFN, EC_FUNCTION_DEFN)) extends TOKEN[(PLAYER_DEFN, EC_FUNCTION_DEFN)] {}
class EC_FUNCTION_DEFN(val value: meta.Tree) extends TOKEN[meta.Tree] {}
class GAME_OUTCOME(val value: String) extends TOKEN[String] {}

class INPUT_LIST_DEFN(val value: List[INPUT_DEFN]) extends TOKEN[List[INPUT_DEFN]] with PART_DEFN {}
class INPUT_DEFN(val value: (INTEGER_LIT, INTEGER_LIT)) extends TOKEN[(INTEGER_LIT, INTEGER_LIT)] {}
abstract class PLAYSTYLE_DEFN(val value: String) extends TOKEN[String] with PART_DEFN {}
abstract class TURNSTYLE_DEFN(val value: String) extends TOKEN[String] with PART_DEFN {}
abstract class ACTION_DEFN(val value: String) extends TOKEN[String] {}

object ANTAGONISTIC extends PLAYSTYLE_DEFN("ANTAGONISTIC")
object OPORTUNISTIC extends PLAYSTYLE_DEFN("OPORTUNISTIC")
object COOPERATIVE extends PLAYSTYLE_DEFN("COOPERATIVE ")

object SEQUENTIAL extends TURNSTYLE_DEFN("SEQUENTIAL")
object SIMULTANEOUS extends TURNSTYLE_DEFN("SIMULTANEOUS")
object MIXED extends TURNSTYLE_DEFN("MIXED")
object FIXED_TIME extends TURNSTYLE_DEFN("FIXED_TIME")

object PUSH extends ACTION_DEFN("Push")
object POP extends ACTION_DEFN("Pop")
object PLACE extends ACTION_DEFN("Place")

object WIN extends GAME_OUTCOME("Win")
object LOSE extends GAME_OUTCOME("Lose")
object DRAW extends GAME_OUTCOME("Draw")
object PENDING extends GAME_OUTCOME("Pending")

object PREVIOUS_PLAYER extends PLAYER_DEFN(STRING_LIT("PreviousPlayer"))
object CURRENT_PLAYER extends PLAYER_DEFN(STRING_LIT("CurrentPlayer"))
object NEXT_PLAYER extends PLAYER_DEFN(STRING_LIT("NextPlayer"))
object ANY_PLAYER extends PLAYER_DEFN(STRING_LIT("AnyPlayer"))
object ALL_PLAYERS extends PLAYER_DEFN(STRING_LIT("Allplayers"))
object NO_PLAYER extends PLAYER_DEFN(STRING_LIT("NoPlayer"))
object NULL_PLAYER extends PLAYER_DEFN(STRING_LIT("NullPlayer"))

object NAME {
  def apply(tok: meta.Tree): NAME = tok match {
    case q"${lit: Lit}" => {
      lit.value match {
        case v: String => new NAME(v)
        case _ => throw new Exception("Error!")
      }
    }
  }
  def apply(s: String): NAME = new NAME(s)
}

object GAME_ID {
  def apply(tok: meta.Tree): GAME_ID = {
    new GAME_ID(NAME(tok)) 
  }
}

object GAME_DEFN {
  def apply(tok: meta.Tree): GAME_DEFN = {
    tok match {
      case q"..$mods object ${name: Term.Name} extends $template" => {
        template match {
          case template"{ ..$stats } with ..$ctorcalls { $param => ..$members }" => {
            ctorcalls.map(_ match {
              // extends GameRuleSet("<gameId>")
              case q"$gRS($gameId)" => {
                gRS match {
                  case ctor"${ctorname: Ctor.Name}" => {
                    //TODO: ctorname.value must be GameRuleSet
                    //println(s"ctorname: ${ctorname.value}")
                  }
                }
                GAME_DEFN(GAME_ID(gameId), MEMBERS(members))
              }
            }).head
          }
        }
      }
    }
  }
  def apply(gID: GAME_ID, members: MEMBERS) = new GAME_DEFN((gID, members))
}

object INTEGER_LIT {
  def apply(i: Int): INTEGER_LIT = new INTEGER_LIT(i)
  def apply(tok: meta.Tree): INTEGER_LIT = tok match {
    case q"${lit: Lit}" => {
      lit.value match {
        case v: String => new INTEGER_LIT(v.toInt)
        case v: Int => new INTEGER_LIT(v)
        case _ => throw new Exception("Error!")
      }
    }
  }
}

object FLOATING_POINT_LIT {
  def apply(i: Float): FLOATING_POINT_LIT = new FLOATING_POINT_LIT(i)
  def apply(tok: meta.Tree): FLOATING_POINT_LIT = tok match {
    case q"${lit: Lit}" => {
      lit.value match {
        case v: String => new FLOATING_POINT_LIT(v.toFloat)
        case v: Float => new FLOATING_POINT_LIT(v)
        case _ => throw new Exception("Error!")
      }
    }
  }
}

object BOOLEAN_LIT {
  def apply(i: Boolean): BOOLEAN_LIT = new BOOLEAN_LIT(i)
  def apply(tok: meta.Tree): BOOLEAN_LIT = tok match {
    case q"${lit: Lit}" => {
      lit.value match {
        case v: String => new BOOLEAN_LIT(v.toBoolean)
        case v: Boolean => new BOOLEAN_LIT(v)
        case _ => throw new Exception("Error!")
      }
    }
  }
}

object CHAR_LIT {
  def apply(i: Char): CHAR_LIT = new CHAR_LIT(i)
  def apply(tok: meta.Tree): CHAR_LIT = tok match {
    case q"${lit: Lit}" => {
      lit.value match {
        case v: String => new CHAR_LIT(v.head)
        case _ => throw new Exception("Error!")
      }
    }
  }
}

object STRING_LIT {
  def apply(s: String): STRING_LIT = new STRING_LIT(s)
  def apply(tok: meta.Tree): STRING_LIT = tok match {
    case q"${lit: Lit}" => {
      lit.value match {
        case v: String => new STRING_LIT(v)
        case _ => throw new Exception("Error!")
      }
    }
  }
}
object MEMBERS {
  val printStuff = true;
  def printPart(s: String, finished: Boolean = true) = if (printStuff || !finished) println(s)
  def apply(toks: Seq[meta.Tree]): MEMBERS = new MEMBERS(
    toks.map(t => {
      getValNameExpr(t) match {
        case ("players", t)       => {
          val retVal = (new PART_NAME("players"),       PLAYER_LIST_DEFN(t))
          printPart(s"PLAYER_LIST_DEFN: ${retVal}")
          retVal
        }
        case ("playStyle", t)     => {
          printPart(s"PLAYSTYLE_DEFN: ${PLAYSTYLE_DEFN(t)}")
          (new PART_NAME("playStyle"),     PLAYSTYLE_DEFN(t))
        }
        case ("turnStyle", t)     => {
          printPart(s"TURNSTYLE_DEFN: ${TURNSTYLE_DEFN(t)}")
          (new PART_NAME("turnStyle"),     TURNSTYLE_DEFN(t))
        }
        case ("graph", t)         => {
          printPart(s"GRAPH_DEFN: ${GRAPH_DEFN(t)}")
          (new PART_NAME("graph"),         GRAPH_DEFN(t))
        }
        case ("legalMoves", t)    => {
          val retVal = (new PART_NAME("legalMoves"),    LEGALMOVE_LIST_DEFN(t))
          printPart(s"LEGALMOVE_DEFN: ${retVal}")
          retVal
        }
        case ("pieces", t)        => {
          val retVal = (new PART_NAME("pieces"),        PIECE_LIST_DEFN(t))
          printPart(s"PIECE_DEFN: ${retVal}")
          retVal
        }
        case ("endConditions", t) => {
          val retVal = (new PART_NAME("endConditions"), ENDCONDITION_LIST_DEFN(t))
          printPart(s"ENDCONDITION_LIST_DEFN: ${retVal}")
          retVal
        }
        case ("inputs", t)        => {
          val retVal = (new PART_NAME("inputs"),        INPUT_LIST_DEFN(t))
          printPart(s"INPUT_LIST_DEFN: ${retVal}", false)
          retVal
        }
      }
    }).toMap)
}
object PART_NAME {
  def apply(tok: meta.Tree): PART_NAME = new PART_NAME("stub") 
}
object GRAPH_DEFN {
  def apply(tok: meta.Tree): GRAPH_DEFN = {
    lazy val d = decomposeGraph(tok)
    new GRAPH_DEFN((decomposeNodeList(d.head), decomposeEdgeList(d.last)))
  }
  def decomposeGraph(tok: meta.Tree): Seq[meta.Tree] = {
    val d = decomposeThing("Graph")(tok)._2
    d
  }
  def decomposeNodeList(tok: meta.Tree): List[NODE_DEFN] = {
    val d = decomposeList(tok)._2
    d.map(NODE_DEFN(_)).toList
  }
  def decomposeEdgeList(tok: meta.Tree): List[EDGE_DEFN] = {
    val d = decomposeList(tok)._2
    d.map(EDGE_DEFN(_)).toList
  }
}
object NODE_DEFN {
  def apply(x: Int, y: Int): NODE_DEFN = new NODE_DEFN((x, y))
  def apply(tok: meta.Tree): NODE_DEFN = new NODE_DEFN(decomposeNodeArgs(tok))
  def decomposeNodeArgs(tok: meta.Tree): (INTEGER_LIT, INTEGER_LIT) = {
    val d = decomposeThing("PointNode")(tok)._2.map(decomposeTuple2(_))
    require(d.size == 1, s"A NODE_DEFN's only take 1 argument: Got ${d.size}")
    require(d.head.size == 2, s"A NODE_DEFN's only takes Tuple2's: Got a Tuple${d.head.size}")
    (INTEGER_LIT(d.head.head), INTEGER_LIT(d.head.last))
  }
}
object EDGE_DEFN {
  def apply(tok: meta.Tree): EDGE_DEFN = new EDGE_DEFN(decomposeEdgeArgs(tok))
  def decomposeEdgeArgs(tok: meta.Tree): (NODE_DEFN, NODE_DEFN) = {
    val d = decomposeThing("BidirectionalEdge")(tok)._2
    (NODE_DEFN(d.head), NODE_DEFN(d.last))
  }
}
object PLAYSTYLE_DEFN {
  def apply(tok: meta.Tree): PLAYSTYLE_DEFN = tok match {
      case q"${name: Term.Name}" => name.value match {
        case "Antagonistic" => ANTAGONISTIC
        case "Oportunistic" => OPORTUNISTIC
        case "Cooperative" => COOPERATIVE
      }
    }
}
object TURNSTYLE_DEFN {
  def apply(tok: meta.Tree): TURNSTYLE_DEFN = tok match {
      case q"${name: Term.Name}" => name.value match {
        case "Sequential" => SEQUENTIAL
        case "Simultaneous" => SIMULTANEOUS
        case "Mixed" => MIXED
        case "FixedTime" => FIXED_TIME
      }
    }
}
object PLAYER_LIST_DEFN {
  def apply(tok: Seq[meta.Tree]): PLAYER_LIST_DEFN = new PLAYER_LIST_DEFN(tok.map(t => PLAYER_DEFN(t)).toList)
  def apply(tok: meta.Tree): PLAYER_LIST_DEFN = PLAYER_LIST_DEFN(listContents(tok))
  def apply(ls: List[String]): PLAYER_LIST_DEFN = new PLAYER_LIST_DEFN(ls.map(PLAYER_DEFN(_)))
}
object PLAYER_DEFN {
  def apply(tok: meta.Tree): PLAYER_DEFN = 
    tok match {
      case q"${name: Term.Name}(${playerName: Lit})" => PLAYER_DEFN(decomposePlayerName(tok))
      case q"${playerName: Term.Name}" => playerName.value match {
        case "PreviousPlayer" => PREVIOUS_PLAYER
        case "CurrentPlayer" => CURRENT_PLAYER
        case "NextPlayer" => NEXT_PLAYER
        case "AnyPlayer" => ANY_PLAYER 
        case "AllPlayers" => ALL_PLAYERS 
        case "NoPlayer" => NO_PLAYER  
        case "NullPlayer" => NULL_PLAYER
      }
    }
  def apply(sl: STRING_LIT): PLAYER_DEFN = new PLAYER_DEFN(sl)
  def apply(l: String): PLAYER_DEFN = PLAYER_DEFN(STRING_LIT(l))

  def decomposePlayerName(tok: meta.Tree): STRING_LIT = {
    val decomposition = decomposeThing("Player")(tok)._2
    require(!(decomposition.size > 1), "A player may not have more than one name")
    require(!(decomposition.size < 1), "A player must have at least one name")
    STRING_LIT(decomposition.head)
  }
}
object PIECE_LIST_DEFN {
  def apply(tok: Seq[meta.Tree]): PIECE_LIST_DEFN = new PIECE_LIST_DEFN(tok.map(t => PIECE_DEFN(t)).toList)
  def apply(tok: meta.Tree): PIECE_LIST_DEFN = PIECE_LIST_DEFN(listContents(tok))
}
object PIECE_DEFN {
  def apply(tok: meta.Tree): PIECE_DEFN = {
    val t = decomposeFunctionApplication(tok)
    PIECE_DEFN(STRING_LIT(t._2(0)), PLAYER_DEFN(t._2(1)))
  }
  def apply(strLit: STRING_LIT, playerDefn: PLAYER_DEFN): PIECE_DEFN = new PIECE_DEFN((strLit, playerDefn))
}
object LEGALMOVE_LIST_DEFN {
  def apply(tok: meta.Tree): LEGALMOVE_LIST_DEFN = {
    val contents = listContents(tok)
    new LEGALMOVE_LIST_DEFN(contents.map(LEGALMOVE_DEFN(_)).toList)
  }
}

object LEGALMOVE_DEFN {
  def apply(tok: meta.Tree): LEGALMOVE_DEFN = {
    val (name, args) = decomposeFunctionApplication(tok)
    new LEGALMOVE_DEFN((
      PLAYER_DEFN(args(0)), 
      STRING_LIT(args(1)), //Piece name
      LM_FUNCTION_DEFN(args(2)), 
      ACTION_DEFN(args(3))
    ))
  }
}
object LM_FUNCTION_DEFN {
  def apply(tok: meta.Tree): LM_FUNCTION_DEFN = new LM_FUNCTION_DEFN(tok)
}
object EC_FUNCTION_DEFN {
  def apply(tok: meta.Tree): EC_FUNCTION_DEFN = new EC_FUNCTION_DEFN(tok)
}
object GAME_OUTCOME {
  def apply(tok: meta.Tree): GAME_OUTCOME = tok match {
      case q"${outcome: Term.Name}" => outcome.value match {
        case "Win" => WIN
        case "Lose" => LOSE
        case "Draw" => DRAW
        case "Pending" => PENDING
      }
    }
}
object ACTION_DEFN {
  def apply(tok: meta.Tree): ACTION_DEFN = tok match {
      case q"${action: Term.Name}" => action.value match {
        case "Push" => PUSH
        case "Pop" => POP
        case "Place" => PLACE
      }
    }
}

object ENDCONDITION_LIST_DEFN {
  def apply(tok: meta.Tree): ENDCONDITION_LIST_DEFN = {
    val contents = listContents(tok)
    new ENDCONDITION_LIST_DEFN(contents.map(ENDCONDITION_DEFN(_)).toList)
  }
}
object ENDCONDITION_DEFN {
  def apply(tok: meta.Tree): ENDCONDITION_DEFN = {
    val (name, args) = decomposeFunctionApplication(tok)
    new ENDCONDITION_DEFN((
      PLAYER_DEFN(args(0)), 
      EC_FUNCTION_DEFN(args(1))
    ))
  }
}
object INPUT_LIST_DEFN {
  def apply(tok: meta.Tree): INPUT_LIST_DEFN = {
    val contents = listContents(tok)
    new INPUT_LIST_DEFN(contents.map(INPUT_DEFN(_)).toList)
  }
}
object INPUT_DEFN {
  def apply(x: INTEGER_LIT, y: INTEGER_LIT) = new INPUT_DEFN((x, y))
  def apply(tok: meta.Tree): INPUT_DEFN = {
    val coords = decomposeFunctionApplication(tok)._2.map(decomposeFunctionApplication(_)._2).flatten
    INPUT_DEFN(INTEGER_LIT(coords(0)), INTEGER_LIT(coords(1)))
  }
}
