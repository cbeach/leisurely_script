# Refactor

## Class structure

Separate Rule definition classes from Play time classes.
A rule that declares the behavior of a player, piece, board, etc. should be separate from a corresponding instance at play time.
A GameRuleSet (Game) should be a factory for playable Game objects.


## Performance Optimization

Immutability is great for development. It's a lot easier to keep track of what the game is doing, but it's not very performant.
There are much better ways of keeping track of game states. 


## Serialization

The GameRuleSet needs to be serialized before it can be stored in GameRepositories.
