package beachc

import beachc.runTime._
import scala.util.{Try, Success, Failure}
import scala.annotation.tailrec

package object Interfaces {
  case class TicTacToe_CLIInterface(gs: TicTacToe) {
    def getInput(source: () => Int) = {
      def getChoice(source: () => Int, prompt: () => String, choices: () => String): Int = {
        println(choices())
        print(prompt())
        Try(source()).recover({
          case e: NumberFormatException => {
            println("please enter a valid number")
            getChoice(source, prompt, choices)
          }
        }).get
      }

      println("\n\n")
      println(s"--------------------------------------------------------------------------------")
      println("Which space would you like to place a piece in?")
      val choice: Int = getChoice(
        source,
        () => {
          s"${gs.players(gs.currentPlayer)}${"$"} "
        }, () => {
          gs.inputs.view.zipWithIndex.map({
            case (input: ButtonInput[DiscreteCoord2D], i: Int) => s"$i. ${input.label}"
          }).mkString("\n")
        }
      )
      println(s"--------------------------------------------------------------------------------")
      gs.inputs(choice).trigger(gs.getCurrentPlayer)
    }
    def printBoard() = {
      val E = """|EEEEEE
                 |E    E
                 |E    E
                 |E    E
                 |EEEEEE""".stripMargin

      val X = """|X    X
                 | X  X 
                 |  XX  
                 | X  X 
                 |X    X""".stripMargin

      val O = """| OOOO
                 |O    O
                 |O    O
                 |O    O
                 | OOOO """.stripMargin

      var board = List(
        List(gs.token_point_0_0, gs.token_point_0_1, gs.token_point_0_2),
        List(gs.token_point_1_0, gs.token_point_1_1, gs.token_point_1_2),
        List(gs.token_point_2_0, gs.token_point_2_1, gs.token_point_2_2)
      )
      board foreach((l) => {
        val lp = l.map(_.pieces match {
          case h :: Nil => h match {
            case Piece_token_X => X
            case Piece_token_O => O
          }
          case Nil => E
        })
        lp(0)
          .split('\n')
          .zip(lp(1).split('\n'))
          .map(_ match {
            case (a: String, b: String) => s"${if (a.length == 5) s"$a " else a}  ${if (b.length == 5) s"$b " else b}"
          })
          .zip(lp(2).split('\n'))
          .map(_ match {
            case (a: String, b: String) => s"$a  ${if (b.length == 5) s"$b " else b}"
          })
          .foreach {
            println(_)
          }
        println
      })
    }
  }
}


