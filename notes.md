#Architectures

## Terms
oracle: The object that represents the game's rules. Can be queried for information about the game's state. 

## Architecture
### Classes
Game
    Game(name:String = None)
        - A UUID is generated if name is not specified
    This is the top level object.
    Methods:
        - add(... ): add one or more objects.
        - load(<game id>): load and import a game oracle from the game repository. The game should be queryable after loading
        - process: process all of the objects that have been added to the game and return the game's oracle.
        - transpile(str <target language>): Produces the code for the oracle in the target language
        - compile(str <compiler command>): Compile a static library from the code that was generated with the transpile method, and
                                           registers is with the game repo. The library includes python language hooks.
Board
    Board(size: List[Int], boardShape: BoardShape, neighborType: NeighborTypes, tileShape: Shape)
    n_in_a_row(<run length>, <neighbor type> <piece>):
        returns a custom iterable of board nodes (tiles) that has custom functions 
        for creating expressive logic expressions 
        - all(): returns an expression object equivalent to <expr> and <expr> and <expr> ...
        - any(): returns an expression object equivalent to <expr> or <expr> or <expr> ...
        - same(<attribute name>): Eg. n_in_a_row(3, 'indirect', game.pieces.stone).all().same('color') would be equivalent to
            all(map(lambda tile: tile.color == tiles[0].color, tilesh))
BoardTile
BoardNode
Player
    Player(name=null: String
    - hands
        - A player can have multiple "hands"
        - A hand consists of a group of things that are owned by a player.
        - Hands can contain cards, pieces, dice, tokens, etc.
        - Hands have several properties
            - Number of objects in the hand
            - Hand limit
            - Hidden:
                - Hidden from other players
                - Hidden from the owner
                - Hidden from everyone
                - Hidden from arbitrary list of players

    hand_properties = {
        'number': 4, 
        'draw_phase': 'end_of_turn', 
        'draw_behavior': ('refill_to', 4), 
        'deck': pegs.deck, 
        'hidden': True,
    }
Piece
Input
Move
EndCondition
Type
Variable
Expression

Card
Deck
Dice

### Functions
#### gdl.logic
    and
    or
    not

## Examples 
### TicTacToe

    from gdl import Game, Board, Player, Piece, End, Move
    from gdl.logic import and, or, not

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
            condition=not(
                and(tic_tac_toe.board.n_in_a_row(3, 'indirect', tic_tac_toe.pieces.stone), 
                    tic_tac_toe.board.is_full()
                )
            ),
        ),
    ))


### peg game

    from gdl import Game, Board, Player, Piece, End, Move, Deck, Card
    from gdl.logic import and, or, not

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
                    precondition=and(
                        pegs.board.is_space_empty(dest),
                        or(
                            pegs.players.current.hand.contains(pegs.deck[dest.color]), 
                            pegs.players.current.hand.contains(pegs.deck['Wild'])
                        )
                    ),
                    action=[game.players.current.hand.discard(pegs.deck[dest.color]), 
                            pegs.board.place_piece(dest)]
                ),
                Move(
                    precondition=and(
                        not(pegs.board.is_space_empty(dest)),
                        pegs.players.current.hand.contains(pegs.deck['Remove'])
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


### Checkers

    from gdl import Game, Player, Piece, End, Move, chess_board
    from gdl.logic import and, or, not

    checkers = Game('Checkers')
    # chess_board is a standard square 8 x 8 board with square tiles that alternate colors
    checkers.add(chess_board.neighbor_type('no_direct'))


### MunchkiN

    # This'll take a while...

## Open questions

How about switching to Scala?
 - Pros
    - Statically typed
    - Better operator overloading
    - Apache Spark ready
    - Performance is a *lot* better than Python. I might not have to even transpile the code.
    - Many interesting and potentially useful language features
    - Will learn Scala better   
 - Cons
   - Slower to develop
   - Don't know it as well
   - 

How am I going to handle adding arbitrary graphs to the board?
 * Should I treat the graph as a member? or should I wrap it entirely?

How do I handle things that haven't been described yet?
 * Or I could enforce strict ordering when adding game elements
     * This is the way to go
     * Easier for users to understand, and probably less complex too.


