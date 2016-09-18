package beachc.ast

package object graphs {
  object CartesianGraph {
    def apply(x: Int, y: Int, n: NeighborType): Graph = Graph(generateNodes(x, y), generateEdges(x, y, n))
    def generateNodes(x: Int, y: Int): List[Node] = {
      var nodes = List[Node]()
      for (i <- 0 until x) {
        for (j <- 0 until y) {
          nodes = Point(Coordinate(i, j)) :: nodes
        }
      }
      nodes
    }
    def generateEdges(x: Int, y: Int, neighborType: NeighborType): List[Edge] = {
      var edges = List[Edge]()
      for (i <- 0 until x) {
        for (j <- 0 until y) {
          if (neighborType != NoDirect) {
            // North
            if (j > 0) {
              edges = CardinalEdge(Point(Coordinate(i, j)), Point(Coordinate(i, j - 1)) , North) :: edges
            }

            // South
            if (j < y - 1) {
              edges = CardinalEdge(Point(Coordinate(i, j)), Point(Coordinate(i, j + 1)), South) :: edges
            }

            // East
            if (i > 0) {
              edges = CardinalEdge(Point(Coordinate(i, j)), Point(Coordinate(i - 1, j)), East) :: edges
            }

            // West
            if (i < x - 1) {
              edges = CardinalEdge(Point(Coordinate(i, j)), Point(Coordinate(i + 1, j)), West) :: edges
            }
          }

          if (neighborType == Indirect || neighborType == NoDirect) {
            // NorthEast
            if (i > 0 && j > 0) {
              edges = CardinalEdge(Point(Coordinate(i, j)), Point(Coordinate(i - 1, j - 1)), NorthEast) :: edges
            }

            // NorthWest
            if (i < x - 1 && j > 0) {
              edges = CardinalEdge(Point(Coordinate(i, j)), Point(Coordinate(i + 1, j - 1)), NorthWest) :: edges
            }

            // SouthEast
            if (i > 0 && j < y - 1) {
              edges = CardinalEdge(Point(Coordinate(i, j)), Point(Coordinate(i - 1, j + 1)), SouthEast) :: edges
            }

            // SouthWest
            if (i < x - 1 && j < y - 1) {
              edges = CardinalEdge(Point(Coordinate(i, j)), Point(Coordinate(i + 1, j + 1)), SouthWest) :: edges
            }
          }
        }
      }
      edges
    }
  }
  val TTTBoard = CartesianGraph(3, 3, Indirect)
  val ChessBoard = CartesianGraph(8, 8, Indirect)
  val CheckerBoard = CartesianGraph(8, 8, NoDirect)
  val GoBoard = CartesianGraph(19, 19, Direct)
}

