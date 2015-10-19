# From beginning to end

1. Create a game: TicTacToe
    1. Get a blank game object from game()
    2. Create TicTacToe using the rules provided in TicTacToe_example.md

2. Start a game of TicTacToe
    1. Get TicTacToe (imported from repo, or created from scratch)
    2. Create an interface with both players using stdin, bind it to the game
        - In this case, the game is the controller, and the interface is the view
        - Interface gets the inputs from the game, binds them to a prompt
        - Input objects handle error conditions
        - Interface contains prompts for the players
    3. call "TicTacToe start"
    4. Get all available moves for player 1
        naive solution    

        game.legalMoves(game.currentPlayer)
        // scope: game.legalMoves
        for (p <- pieces) {
            // Need access to the parent game object so temporary moves can be generated
            p.legalMoves(this)        
        }

        // scope: Piece.legalMoves
        for (m <- moves) {
            for (node <- game.board.nodes) {
                test precondition
                perform move temporarily
                test postcondition
            }
        }

    5. Make player 1's move
        
        game.applyMove(move)
        // scope: game.applyMove
        for (p <- pieces) {
            p.legal(move)
        }
        // scope: Piece.legal
        for (m <- moves) {
            m.legal(move)
        }
        // move succeeds if any LegalMove return back legal

    6. check game's state (has the game ended)
    7. get all available moves for player 2
    8. Make player 2's move
    9. If board is not full, go to 3
    10. Display game results 
