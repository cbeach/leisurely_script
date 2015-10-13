# From beginning to end

1. Create a game: TicTacToe
    1. Get a blank game object from game()
    2. Create TicTacToe using the rules provided in tic_tac_toe_example.md

2. Start a game of TicTacToe
    1. Get TicTacToe (imported from repo, or created from scratch)
    2. Create an interface with both players using stdin, bind it to the game
        - In this case, the game is the controller, and the interface is the view
        - Interface gets the inputs from the game, binds them to a prompt
        - Input objects handle error conditions
        - Interface contains prompts for the players
    3. call "tic_tac_toe start"
    4. Get all available moves for player 1
    5. Make player 1's move
    6. get all available moves for player 2
    7. Make player 2's move
    8. If board is not full, go to 3
    9. Display game results 
