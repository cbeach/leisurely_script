#Architecture

## Terms
oracle: The object that represents the game's rules. Can be queried for information about the game's state. 

## Libraries
scalaTest

    libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

spray-json

    libraryDependencies += "io.spray" %%  "spray-json" % "1.3.2"

scala-graph

    libraryDependencies += "com.assembla.scala-incubator" %% "graph-core" % "1.9.4"


## Classes and Functions

### Views
Write views for spray-json to serialize/deserialize games, boards, players, etc.


### GameRepository
Requests a serialized game definition from a repository. Possible sources:
* GitGames: from a git repo
* CouchbaseGames, SQLGames, CassandraGames, ElasitcGames, etc.: from a datastore/database
* GameFile: from the file system 
* WebGames: from a web-api

- submit(game:Game): Try
    - Put a game into the repo
- load(gameID:String): Try
    - Read a game from the repo
- update(gameID:String, game:Game): Try
    - Update an existing game
- remove(gameID:String)


### Game (class)
- add( ... ): Try[Game]
    - add one or more objects (Board, Player, Piece, etc.)
- start(interface:Interface): Try[Game]
    - Start playing a game. Returns failure if there's something wrong with the game rules.
- gameValid(): Try[Game]
    If game is not valid, return Failure with the exception that was captured
    - Should check for:
        * Is there a Board?
        * Are there one or more of the following, and are they all valid?
            * Players?
            * Pieces?
                * Do all of the pieces have one or more LegalMoves?
            * EndConditions?
                * Are the conditions valid?
- inputs(): Map[String, Input]
- player.currentPlayer(): Player
- legalMoves(player: Player): List[Move]
    - Iterate through each move for each piece that player owns, returning all moves that return true for pre and post conditions.
- isMoveLegal(move:Move): Boolean
- partialScore(): List[Double]
- partialScore(player: Player): Double
- gameResult(): GameResult
- applyMove[T1, T2, T3...](input:Input*): Try[Game] 
    - Each type parameter applies to a different input
- nonValidatedApplyMove(move:Move): Try[Game]
- applyMove(move:Move): Try[Game]

- isMoveTerminal(move:Move): Boolean
- moveResults(move:Move): GameResults

- history(): List[Game]
- various getters and setters
    - board(): Board

// Now that I'm using Scala I probably don't need this stuff until much, much later.
- process: process all of the objects that have been added to the game and return the game's oracle.
- transpile(str <target language>): Produces the code for the oracle in the target language
- compile(str <compiler command>): Compile a static library from the code that was generated with the transpile method, and registers is with the game repo. The library includes python language hooks.


### Game (object)
Basically a game factory. 

- Game(name:String = None): Game
    - A UUID is generated if name is not specified
- Game(repo:GameRepository): Game
    Fetches a serialized game from the specified repo
- apply(<game id>): Try[Game]
    - load and import a game oracle from the game repository. The game should be queryable after loading

The lines:

    val game1 = Game()
    val game2 = Game("TicTacToe")

Will yield two empty games that the developer can then fill with asset definitions. The first game's name is a UUID, the second game's name is "TicTacToe".

    val gitGame = GitGame(<branch/repo/commit/something>)
    val webGame = WebGames(<baseURL>).get(<gameID>)

Download a serialized game from the a git repo and the internet respectively.


### Board
Possible graphing libraries
- scalax.collection.Graph
    - Nope nope nope nope. Literally crashed the scala compiler :/

- Looks like I'm rolling my own :(


Class members
- Board(size: List[Int], boardShape: Shape, neighborType: NeighborTypes, nodeShape: Shape)

// Manipulate existing boards while performing AI operations
- Clone():Board
- Add(p:Piece, coords:Int*) // Add a piece to a node

- nInARow(<run length>:Int, <neighbor type>:NeighborType=null, <piece>:Piece=null): returns a custom iterable of board nodes that has methods for creating expressive logic expressions 
    - Note: Starting out with the assumtion that we want n in a row from the same player.
    - all(): returns an expression object equivalent to <expr> and <expr> and <expr> ...
    - any(): returns an expression object equivalent to <expr> or <expr> or <expr> ...
    - same(<attribute name>): Eg. n_in_a_row(3, 'indirect', game.pieces.stone).all().same(Owner) would be equivalent to
        all(map(lambda node: node.piece.owner == nodes[0].owner, node))
- empty():Boolean
- full():Boolean

// Future
- Remove(coords:Int*) // Remove a piece from a node 


### BoardNode
- empty:Boolean
- pieces(): List[Piece]


### Player
How should I handle things like Piece("name", player=all...)
It needs to handle 
    * Previous
    * Current
    * Next
    * Any
    * All
    * SomePlayers
    * NoPlayer

I could use:
    Almost all of these run into a chicken and egg problem, just write a simple kludge and refactor later.
    1. Companion object and a set of sub-classes
        Most versitile solution so far
        More complex than an enumeration or a function
        Difficult to get the correct game object into the mix
    2. A class and an enumeration
        Set player constraint on instantiation
        Method to check whether player p is valid given the constraint
        Difficult to get the correct game object into the mix
    2. A class and an enumeration
        Difficult to get the correct game object into the mix
    3. An enumeration.
        Easy, simple, very limited
    4. Functions that return the proper players
    5. A set of objects that extend Player
    6. Could be member functions of the Players class
        Already has the complete list of players

class Player(...) {...}
object Player {
    apply(...)
    previous:Player
    current:Player
    next:Player
    any:Player
    all:Player
    some:Player
    none:Player
}

    Player(name=null: String)
    Future
    - Hands
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


#### PlayerObjects
These objects will probably provide the actual players by getting them from the game object
- object allPlayers extends Player
- object currentPlayer
- object previousPlayer
- object nextPlayer

    
### Piece
- val name: String
- val owner: Player
- val moves: List[LegalMove]
- legalMoves(game:Game)(player: Player): List[Move]

// Future
- val attributes: Map[String, Attribute]


### Input[T <: Double]
How do I get the information from the input to the destination variable?
    - Callback?
    - Bind both sides of a variable (reference?) to the input and destination
    - Automatically create hooks for the input based on the list of legal moves
    - Make the game object create inputs, and have the interface use them. The developer would not deal with them. (I like this one the most so far)
- Input[T]()
- val value: () => T


### LegalMove
- val owner: Player
- val precondition: () => Boolean
- val action: Action
- val postcondition: () => Boolean


### EndCondition
- val result: GameResult
- val player: Player
- val condition: () => Boolean


### MoveAction
- Enumeration
    * push
    * pop


### GameResultState
- Enumeration
    * Win
    * Lose
    * Tie
    * Pending


### GameResult
- Case Class
    * val status:GameStatus 
    * val ranking:List[List[Player]]


### GameStatus
- Enumeration
    * Invalid
    * Beginning
    * InProgress
    * Completed


### Shape
    Enumeration
    * Triangle
    * Square
    * Rectangle
    * Hexagon
    * Octagon


### NeighborTypes
    Enumeration
    * Direct        // Default for most uses
    * Indirect      // Diagonal
    * NoDirect      // Implies Indirect is also True in most (all?) cases


### Move
- val piece: Piece
- val player: Player
- val action: MoveAction
- val node: BoardNode


### Card

### Deck

### Dice

### Attribute


### Equipment
    Trait that is used by Pieces, Cards, Decks, Dice, etc.

### Interface
- Interface(inputs:List[Input], players:Player)

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


### Order of Implementation
Start at the leaves and work your way up

Game
    Player
    Board
        Node
    Piece
        Player
        LegalMove
        MoveAction
    EndConditions
        Player
        GameResult

1. Player
2. Game
    - Stub with methods for getting players (current, previous, next, all, etc.)
3. MoveAction
4. LegalMove
    - Requires Game to have an applyMove method
5. Piece
6. GameResult
7. EndConditions
8. Board
9. Graph
    - Node needs to contain references to its contents
10. Move
11. Game
    - applyMove
12. GameFactory
13. Input
14. Game
    - inputs
15. Interface
16. Game
    - interface
17. GameRepository
