#Architecture

## Terms
oracle: The object that represents the game's rules. Can be queried for information about the game's state. 

### Classes
#### Game
- add( ... ): Try[Game]
    - add one or more objects (Board, Player, Piece, etc.)
- start(interface:Interface): Try[Game]
    - Start playing a game. Returns failure if there's something wrong with the game rules.

// Now that I'm using Scala I probably don't even need this
- process: process all of the objects that have been added to the game and return the game's oracle.
- transpile(str <target language>): Produces the code for the oracle in the target language
- compile(str <compiler command>): Compile a static library from the code that was generated with the transpile method, and registers is with the game repo. The library includes python language hooks.


##### GameObject
    This is the top level object.
- Game(name:String = None): A UUID is generated if name is not specified
- apply(<game id>): Try[Game]
    - load and import a game oracle from the game repository. The game should be queryable after loading


#### Board
- Board(size: List[Int], boardShape: BoardShape, neighborType: NeighborTypes, tileShape: Shape)

// Manipulate existing boards while performing AI operations
- Clone():Board
- Add(p:Piece, coords:Int*) // Add a piece to a node
- Remove(coords:Int*) // Remove a piece from a node 

- nInARow(<run length>, <neighbor type> <piece>): returns a custom iterable of board nodes (tiles) that has custom functions for creating expressive logic expressions 
    - all(): returns an expression object equivalent to <expr> and <expr> and <expr> ...
    - any(): returns an expression object equivalent to <expr> or <expr> or <expr> ...
    - same(<attribute name>): Eg. n_in_a_row(3, 'indirect', game.pieces.stone).all().same('color') would be equivalent to
        all(map(lambda tile: tile.color == tiles[0].color, tilesh))
- boardEmpty
- tile(coords: Int*): BoardNode


#### BoardNode
- empty:Boolean


#### Player
    Player(name=null: String)
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
        'drawPhase': 'endOfTurn', 
        'drawBehavior': ('refillTo', 4), 
        'deck': pegs.deck, 
        'hidden': True,
    }

##### PlayerObjects
These objects will probably provide the actual players by getting them from the game object
- object allPlayers extends Player
- object currentPlayer
- object previousPlayer
- object nextPlayer

    
#### Piece
- val name: String
- val owner: Player
- val moves: List[LegalMove]

// Future
- val attributes: Map[String, Attribute]

#### Input[T <: Double]
- val value: () => T

#### MoveAction
- push
- pop

#### LegalMove
- val owner: Player
- val precondition: () => Boolean
- val action: Action
- val postcondition: () => Boolean

#### EndCondition
- val result: GameResult
- val player: Player
- val condition: () => Boolean

#### GameResult
- Enumeration
    * Win
    * Lose
    * Tie


#### Variable

#### Shape
    Enumeration
    * Triagle
    * Square
    * Rectangle
    * Hexagon
    * Octogon

#### Card

#### Deck

#### Dice

#### Attribute

#### Interface

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


