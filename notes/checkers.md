# Checkers

## Code

    from gdl import Game, Player, Piece, End, Move, chess_board
    from gdl.logic import and, or, not

    checkers = Game('Checkers')
    # chess_board is a standard square 8 x 8 board with square tiles that alternate colors
    checkers.add(chess_board.neighbor_type('no_direct'))


