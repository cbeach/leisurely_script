# TicTacToe

## Example Code

    from gdl import Game, Board, Player, Piece, End, Move

    tic_tac_toe = Game(name='TicTacToe')

    board = Board(
        size=(3, 3), 
        board_shape='square', 
        neighbor_type='indirect', 
        tile_shape='square'
    ).graph.do_stuff_if_you_want()

    tic_tac_toe.add(board)

    tic_tac_toe.add((
        Player('X'), 
        Player('Y')
    ))

    tic_tac_toe.add(
        Piece(
            name='stone', 
            players=tic_tac_toe.players.all,
            moves=(
                Move(
                    precondition=tic_tac_toe.board.is_space_empty(dest),
                    action=tic_tac_toe.board.place_piece(dest)
                ),
            )
        ), 
    )
        
    tic_tac_toe.add((
        End(
            result='win', 
            player=tic_tac_toe.players.current, 
            condition=tic_tac_toe.board.n_in_a_row(3, 'indirect', tic_tac_toe.pieces.stone)
        ),
        End(
            result='draw', 
            tic_tac_toe.players.all, 
            condition=!(
                tic_tac_toe.board.n_in_a_row(3, 'indirect', tic_tac_toe.pieces.stone) 
                && tic_tac_toe.board.is_full()
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
            val neighborBehavior: Neighbors

            // Could use an implicit board generator
            private def createBoardGraph(implicit BoardGenerator[T <: Shape]: graph) = {
                // create graph according to the board shape, dimensions, tile shape, and neighbor behavior.   
            }
            
            def isEmpty: Boolean = {
                ... 
            }

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
