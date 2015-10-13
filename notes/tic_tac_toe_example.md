# TicTacToe

## Example Code

    from gdl import Game, Board, Player, Piece, End, Move

    TicTacToe = Game(name='TicTacToe')

    board = Board(
        size=(3, 3), 
        board_shape='square', 
        neighbor_type='indirect', 
        tile_shape='square'
    ).graph.do_stuff_if_you_want()

    TicTacToe.add(board)

    TicTacToe.add((
        Player('X'), 
        Player('Y')
    ))

    TicTacToe.add(
        Piece(
            name='stone', 
            players=TicTacToe.players.all,
            moves=(
                Move(
                    precondition=TicTacToe.board.tile(dest).empty,
                    action=TicTacToe.board.place_piece(dest)
                ),
            )
        ), 
    )
        
    TicTacToe.add((
        End(
            result='win', 
            player=TicTacToe.players.current, 
            condition=TicTacToe.board.nInARow(3, TicTacToe.pieces.stone)
        ),
        End(
            result='draw', 
            TicTacToe.players.all, 
            condition=!(
                TicTacToe.board.nInARow(3, TicTacToe.pieces.stone) 
                && TicTacToe.board.full
                )
            ),
        ),
    ))

## Generated structure

    Game {
        val board: Board {
            val dimensions: List[Int]
            val boardShape: Shape
            val tileShape: Shape 
            val neighborBehavior: NeighborType

            // Could use an implicit board generator
            private def createBoardGraph(implicit BoardGenerator[T <: Shape]: graph) = {
                // create graph according to the board shape, dimensions, tile shape, and neighbor behavior.   
            }
            
            def empty: Boolean = { ... }

            // Neighbor behavior should be optional. Should use the board's neighborBehavior attribute by default.
            def nInARow(n: Int, piece: Piece): Boolean = {
                // Check the tiles based on the neighbor behavior
            }
        }
        val players: List[Player]
        val pieces: List[Piece]
        val endConditions: List[End]

        def score(b: Board): Double = { }
        def legalMoves(player: Player): List[MoveAction] = { }
        def apply(board: Board) Try[Board] = { }
    }
