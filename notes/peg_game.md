# Peg Game

## Code

    from gdl import Game, Board, Player, Piece, End, Move, Deck, Card

    pegs = Game(name='Peg Game')

    pegs.add(
        Deck(cards=[
            (Card(name='Yellow'), 25),
            (Card(name='Blue'), 25),
            (Card(name='Green'), 25),
            (Card(name='Red'), 25),
            (Card(name='Wild'), 2),
            (Card(name='Remove'), 2),
        ],
        on_empty='reshuffle_discard')
    ))

    hand_properties = {
        'number': 4, 
        'draw_phase': 'end_of_turn', 
        'draw_behavior': ('refill_to', 4), 
        'deck': pegs.deck, 
        'hidden': True,
    }
    pegs.add((
        Player('One').hand(**hand_properties), 
        Player('Two').hand(**hand_properties),
        Player('Three').hand(**hand_properties),
        Player('Four').hand(**hand_properties),
    ))

    # Create the board and assign colors to the quadrants
    pegs.add(Board(size=(10, 10), board_shape='square', tile_shape='square', neighbor_type='indirect'))
    for tile in board.tiles():
        if tile.position[0] < 5 and tile.position[1] < 5: 
            # New attributes are added to the board's nodes (tiles) by simple assignment
            tile.color = 'Yellow'
        elif tile.position[0] >= 5 and tile.position[1] < 5: 
            tile.color = 'Blue'
        elif tile.position[0] < 5 and tile.position[1] >= 5: 
            tile.color = 'Green'
        elif tile.position[0] => 5 and tile.position[1] >= 5: 
            tile.color = 'Red'

    pegs.add(
        Piece(
            name='Peg', 
            players=pegs.players.all,
            moves=(
                Move(
                    precondition=
                        pegs.board.is_space_empty(dest) 
                        && (pegs.players.current.hand.contains(pegs.deck[dest.color]) 
                            || pegs.players.current.hand.contains(pegs.deck['Wild'])
                        ),
                    action=[game.players.current.hand.discard(pegs.deck[dest.color]), 
                            pegs.board.place_piece(dest)]
                ),
                Move(
                    precondition=
                        !pegs.board.is_space_empty(dest)
                        && pegs.players.current.hand.contains(pegs.deck['Remove'])
                    ),
                    action=[game.players.current.hand.discard(pegs.deck['Remove']), 
                            pegs.board.remove_piece(dest)]
            )
        ), 
    )
    pegs.add((
        End(
            result='win', 
            player=pegs.players.current, 
            condition=pegs.board.n_in_a_row(4, 'indirect', pegs.pieces.peg).all().same('color')
        ),
        End(
            result='win', 
            player=pegs.players.current, 
            condition=pegs.board.n_in_a_row(5, 'indirect', pegs.pieces.peg)
        ),
        End(
            result='win', 
            player=pegs.players.current, 
            condition=or(
                pegs.players.current.occupies(
                    ((0, 0), (0, 9), (9, 0), (9, 9),)),
                pegs.players.current.occupies(
                    ((0, 0), (0, 4), (4, 0), (4, 4),)),
                pegs.players.current.occupies(
                    ((0, 5), (0, 9), (4, 5), (4, 9),)),
                pegs.players.current.occupies(
                    ((5, 0), (5, 4), (9, 0), (9, 4),)),
                pegs.players.current.occupies(
                    ((5, 5), (5, 9), (9, 5), (9, 9),)),
            ),
        ),
    ))
