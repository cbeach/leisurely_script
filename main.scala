import beachc.metaprogrammers.GameStateGenerator
import beachc.ast._


object astTest {
  def main(args: Array[String]): Unit = {
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
          BidirectionalEdge(Point((0, 0)), Point((0, 1))),
          BidirectionalEdge(Point((0, 0)), Point((1, 0))),
          BidirectionalEdge(Point((0, 0)), Point((1, 1))),

          BidirectionalEdge(Point((0, 1)), Point((0, 0))),
          BidirectionalEdge(Point((0, 1)), Point((0, 2))),
          BidirectionalEdge(Point((0, 1)), Point((1, 0))),
          BidirectionalEdge(Point((0, 1)), Point((1, 1))),
          BidirectionalEdge(Point((0, 1)), Point((1, 2))),

          BidirectionalEdge(Point((0, 2)), Point((0, 1))),
          BidirectionalEdge(Point((0, 2)), Point((1, 1))),
          BidirectionalEdge(Point((0, 2)), Point((1, 2))),

          BidirectionalEdge(Point((1, 0)), Point((0, 0))),
          BidirectionalEdge(Point((1, 0)), Point((0, 1))),
          BidirectionalEdge(Point((1, 0)), Point((1, 1))),
          BidirectionalEdge(Point((1, 0)), Point((2, 0))),
          BidirectionalEdge(Point((1, 0)), Point((2, 1))),

          BidirectionalEdge(Point((1, 1)), Point((0, 0))),
          BidirectionalEdge(Point((1, 1)), Point((0, 1))),
          BidirectionalEdge(Point((1, 1)), Point((0, 2))),
          BidirectionalEdge(Point((1, 1)), Point((1, 0))),
          BidirectionalEdge(Point((1, 1)), Point((1, 2))),
          BidirectionalEdge(Point((1, 1)), Point((2, 0))),
          BidirectionalEdge(Point((1, 1)), Point((2, 1))),
          BidirectionalEdge(Point((1, 1)), Point((2, 2))),

          BidirectionalEdge(Point((1, 2)), Point((0, 1))),
          BidirectionalEdge(Point((1, 2)), Point((0, 2))),
          BidirectionalEdge(Point((1, 2)), Point((1, 1))),
          BidirectionalEdge(Point((1, 2)), Point((2, 1))),
          BidirectionalEdge(Point((1, 2)), Point((2, 2))),

          BidirectionalEdge(Point((2, 0)), Point((1, 0))),
          BidirectionalEdge(Point((2, 0)), Point((1, 1))),
          BidirectionalEdge(Point((2, 0)), Point((2, 1))),

          BidirectionalEdge(Point((2, 1)), Point((1, 0))),
          BidirectionalEdge(Point((2, 1)), Point((1, 1))),
          BidirectionalEdge(Point((2, 1)), Point((1, 2))),
          BidirectionalEdge(Point((2, 1)), Point((2, 0))),
          BidirectionalEdge(Point((2, 1)), Point((2, 2))),

          BidirectionalEdge(Point((2, 2)), Point((1, 1))),
          BidirectionalEdge(Point((2, 2)), Point((1, 2))),
          BidirectionalEdge(Point((2, 2)), Point((2, 1)))
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
