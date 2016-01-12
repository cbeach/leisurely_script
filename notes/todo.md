# Refactor

## Update style

Changes the tabs and formatting to be consistent with the scala style guidelines


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

Here are some interesting articles on type safe builder patterns in scala (I'm getting a serious black magic vibe from these things)
http://jim-mcbeath.blogspot.com/2009/09/type-safe-builder-in-scala-part-3.html
http://dcsobral.blogspot.com/2009/09/type-safe-builder-pattern.html#sthash.0EbyrUSx.dpuf
http://www.tikalk.com/java/type-safe-builder-scala-using-type-constraints/


## refactor package names

The package names should reflect the directory structure.
