import beachc.metaprogrammers.GameStateGenerator
import beachc.ast._


object astTest {
  def main(args: Array[String]): Unit = {
    val temp: (Int, Int) => List[Int] = (a: Int, b: Int) => {
      List[Int](a, b)
    }
    val input = CoordinateInput

    @GameStateGenerator
    val gRS = GameRuleSet(
      "Tic-Tac-Toe",
      List(
        Person("X", (gs: GameState) => 100),
        Person("Y", (gs: GameState) => 100)
      ),
      Graph(
        List(
          Point((0, 0)), Point((1, 0)), Point((2, 0)), 
          Point((0, 1)), Point((1, 1)), Point((2, 1)), 
          Point((0, 2)), Point((1, 2)), Point((2, 2))
        ),
        List(
          CardinalEdge(Point((0, 0)), Point((0, 1)), East),
          CardinalEdge(Point((0, 0)), Point((1, 0)), South), 
          CardinalEdge(Point((0, 0)), Point((1, 1)), SouthEast),

          CardinalEdge(Point((0, 1)), Point((0, 0)), West),
          CardinalEdge(Point((0, 1)), Point((0, 2)), East),
          CardinalEdge(Point((0, 1)), Point((1, 0)), SouthWest),
          CardinalEdge(Point((0, 1)), Point((1, 1)), South),
          CardinalEdge(Point((0, 1)), Point((1, 2)), SouthEast),

          CardinalEdge(Point((0, 2)), Point((0, 1)), West), 
          CardinalEdge(Point((0, 2)), Point((1, 1)), SouthWest), 
          CardinalEdge(Point((0, 2)), Point((1, 2)), South), 

          CardinalEdge(Point((1, 0)), Point((0, 0)), North),
          CardinalEdge(Point((1, 0)), Point((0, 1)), NorthEast),
          CardinalEdge(Point((1, 0)), Point((1, 1)), East),
          CardinalEdge(Point((1, 0)), Point((2, 0)), South),
          CardinalEdge(Point((1, 0)), Point((2, 1)), SouthEast),

          CardinalEdge(Point((1, 1)), Point((0, 0)), NorthWest),
          CardinalEdge(Point((1, 1)), Point((0, 1)), North),
          CardinalEdge(Point((1, 1)), Point((0, 2)), NorthEast),
          CardinalEdge(Point((1, 1)), Point((1, 0)), West),
          CardinalEdge(Point((1, 1)), Point((1, 2)), East),
          CardinalEdge(Point((1, 1)), Point((2, 0)), SouthWest),
          CardinalEdge(Point((1, 1)), Point((2, 1)), South),
          CardinalEdge(Point((1, 1)), Point((2, 2)), SouthEast),

          CardinalEdge(Point((1, 2)), Point((0, 1)), NorthWest),
          CardinalEdge(Point((1, 2)), Point((0, 2)), North),
          CardinalEdge(Point((1, 2)), Point((1, 1)), West),
          CardinalEdge(Point((1, 2)), Point((2, 1)), SouthWest),
          CardinalEdge(Point((1, 2)), Point((2, 2)), South),

          CardinalEdge(Point((2, 0)), Point((1, 0)), North),
          CardinalEdge(Point((2, 0)), Point((1, 1)), NorthEast),
          CardinalEdge(Point((2, 0)), Point((2, 1)), East),

          CardinalEdge(Point((2, 1)), Point((1, 0)), NorthWest),
          CardinalEdge(Point((2, 1)), Point((1, 1)), North),
          CardinalEdge(Point((2, 1)), Point((1, 2)), NorthEast),
          CardinalEdge(Point((2, 1)), Point((2, 0)), West), 
          CardinalEdge(Point((2, 1)), Point((2, 2)), East),

          CardinalEdge(Point((2, 2)), Point((1, 1)), NorthWest),
          CardinalEdge(Point((2, 2)), Point((1, 2)), North),
          CardinalEdge(Point((2, 2)), Point((2, 1)), West)
        )
      ),
      List(Piece("token", AnyPlayer, 
        LegalMove(
          AnyPlayer, 
          (gs: GameState) => {
            gs.space(0)(input()).isEmpty
          },
          Push("token", input())
        )
      )),
      List(
        EndCondition(
          CurrentPlayer,
          (gS: GameState) => if (gS.ThreeInARow(PreviousPlayer, "token")) {
            Win
          } else {
            Pending
          }
        ),
        EndCondition(
          CurrentPlayer,
          (gS: GameState) => {
            if (gS.boardFull && !gS.ThreeInARow(PreviousPlayer, "token")) {
              Draw
            } else {
              Pending
            }
          }
        )
      )
    )
    gRS.letsPlayAGame()
  }
}
