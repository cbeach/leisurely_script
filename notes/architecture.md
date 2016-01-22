#Architecture

## Terms
oracle: The object that represents the game's rules. Can be queried for information about the game's state. 

## Libraries
scalaTest

    libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

spray-json

    libraryDependencies += "io.spray" %%  "spray-json" % "1.3.2"


## Classes and Functions

### Views
Write views for spray-json to serialize/deserialize games, boards, players, etc.


### GameRepository
Requests a serialized game definition from a repository. Possible sources:
* GitGames: from a git repo
* CouchbaseGames, SQLGames, CassandraGames, ElasitcGames, etc.: from a datastore/database
* GameFile: from the file system 
* WebGames: from a web-api

- submit(game:Game):Unit
    - Put a game into the repo
- load(gameID:String):Try[Game]
    - Read a game from the repo
- update(gameID:String, game:Game):Unit
    - Update an existing game
- remove(gameID:String):Unit


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


#### Future

A feature that might be interesting to try a state/history management technique similar to the way lightning finds its way to the ground.
During AI game play, a LOT of branches are search while deciding which path to take down the tree. These don't affect the actual game until the move is made.
Tracking this information might make it easier to keep track of certain types of information. Then again it violates immutability and encapsulation, so it's 
probably more trouble than it's worth. Might deserve further research.

Now that I'm using Scala I probably don't need this stuff until much, much later.
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


### ConcretelyKnownPlayer
Used for setting ownership during game player. Should be required by pieces of equipment.
- Future:
    - This may be relaxed at a later date

### Player
This is all wrong. I should not make a class that I plan to instantiate the base class of the hierarchy. Bad Casey!
Implement two traites. PlayerValidator and ConcretelyKnownPlayer. The first provides methods that return whether the player is
valid or not. The second is a static, known, group of one or more players that requires no outside information to do its job.
It is used to set ownership _during_ game play.

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

## Refactoring

### Separate the language and implementation.

The "AST" should not be playable, but instead should generate a playable data structure.

#### Game/GameRuleSet

Fairly straight forward, already mostly done.

#### Board/BoardRuleSet

This is more complicated. 
Things I have to decide: 
 * How I want to represent the board's data
 * Whether and how to pre-compute:
   * Victory conditions
   * Legal moves

Data representations
    * The board is represented by a 2D array of linked lists and a list of matrices. 
    * The 2D array of linked list represents the order of stacks.
    * A cell in the array contains a linked list if and only if there is a _stack_ of pieces in the corresponding node. i.e. the cell will contain a linked list
      if there is a stack of pieces (like in checkers), but not if there is just a _group_ of pieces (such as in mancala).
    * A matrix has type Int.
    * There is a matrix for each type of piece defined in the GameRuleSet.
    * Each cell in a matrix represents a count of pieces (that that matrix represents) contained in the corresponding node.
    * Each cell in a matrix represents a node in the graph. 
    * For all matrices the position (x, y) refers to the same node in the graph. 

Methods
    * All play methods in BoardRuleSet
    * Undo method

NInARow Pre-computation
    1. BoardRuleSet.getPlayableBoard is called
    2. getPlayableBoard looks at Conditionals and pre-computes the necessary values

### Conditions

Allowing arbitrary code in conditions is a bit of a problem. It reduces the power of pre-computing board states/transformations.
I could create a sub-dsl that has the required expressiveness while sufficiently reducing the set cardinality of equivalent rules.

Are the previous statements correct? Couldn't a change to the boolean methods be a reasonable alternative?
For instance nInARow could generate its own pre-computed boolean expression and return an object with the appropriate operators overloaded
I.e. de-sugaring game.ruleSet.board.nInARow(3, game.pieces(0).getPhysicalPiece(player)) would return an object of type NInARow that overloads 
all of the boolean operators (&, |, !, ==, etc.).

Yes, the original statement is correct. The alternative allows pre-computation, but not genetic recombination.

#### Examples

EndCondition(Win, PreviousPlayer, (game:Game, player:Player) => {
    game.ruleSet.board.nInARow(3, game.pieces(0).getPhysicalPiece(player)).size > 0
}) ==>

iff nInARow(<Int>, <PhysicalPiece>, <Board>) then previous player wins 

EndCondition(Tie, AllPlayers, (game:Game, player:Player) => {
    game.ruleSet.board.nInARow(3, game.pieces(0).getPhysicalPiece(player)).size == 0 && game.board.full()
})

iff <board> full and not board nInARow(<Int>, <PhysicalPiece>, <Board>) then all players tie 

#### Details

x iff 
nInARow
full
empty
and
not

previous
player
wins

iff (<board>.full && !board.nInARow(<PhysicalPiece>)) { all players tie }
{1} {    2      } {3}{4}{          5               }    {      6      }

1. ConditionalExpression
2. BoardRuleSet.full:BooleanExpression
3. BooleanExpression.&&(other:BooleanExpression):BooleanExpression
4. BooleanExpression.unary_!(other:BooleanExpression):BooleanExpression
5. BoardRuleSet.nInARow(PhysicalPiece):BooleanExpression
6. SequenceExpression[players].tie

trait GameExpression[T] {
    val value:T
    def evaluate:T = value
}

trait PrimitiveExpression[+T <: AnyVal] extends GameExpression[T] {}

class BooleanExpression extends GameExpression[Boolean] {
    def &&, ||, unary_!, etc.
}

trait ConditionalExpression extends GameExpression {}

class iff[T <: GameExpression] extends GameExpression[T] {
    val condition:BooleanExpression
    val then:T
    val otherwise:Option[T]
    def evaluate:T = if (condition.evaluate) {
        
    } else {
        otherwise match {
            case Some(expr:T) => expr.evaluate
            case None => Unit
        }
    }
}

object iff {
    def apply(
}

object iff extends ConditionalExpression {
    def apply(expression:ValueExpression[Boolean]):ConditionalExpression
}

class then extends ConditionalExpression {
    def apply(expression:GameExpression):GameExpression
}

class otherwise extends ConditionalExpression {

}

GameRule methods such as nInARow, full, empty, etc. must return GameExpressions. These game expressions will later be used to create the conditions for the playable game.
