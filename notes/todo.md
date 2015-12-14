# Refactor

## Class structure

Separate Rule definition classes from Play time classes.
A rule that declares the behavior of a player, piece, board, etc. should be separate from a corresponding instance at play time.
A GameRuleSet (Game) should be a factory for playable Game objects.


## Performance Optimization

Immutability is great for development. It's a lot easier to keep track of what the game is doing, but it's not very performant.
There are much better (efficient) ways of keeping track of game states. 


## Builders for game and board classes

The Game and Board classes are a little more complex than most of the other classes in the game tree. They should probably have better builder classes
to aid game creation.


## Take the "edges" variable out of the BoardNode definition.

Moving the references out of the BoardNodes will allow BoardNodes to be immutable.
