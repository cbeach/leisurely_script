COMMENTS             ::= C Style
PRINTABLE_CHAR       ::= [\^ \u00-\u1F\u80-\u9F\u7F]   // Any character outside of the set of unicode control characters
PRINTABLE_CHAR_NSQ   ::= [\^ \u00-\u1F\u80-\u9F\u7F']  // No Single Quotes (NSQ)
PRINTABLE_CHAR_NDQ   ::= [\^ \u00-\u1F\u80-\u9F\u7F"]  // No Double Quotes (NDQ)
ESCAPE_CHAR          ::= \['btnfr"\]

CHARACTERS           ::= CHAR
                      |  CHAR CHARACTERS
CHARACTERS_NDQ       ::= CHAR_NDQ
                      |  CHAR_NDQ CHARACTERS_NDQ
CHAR                 ::= PRINTABLE_CHAR
                      |  ESCAPE_CHAR
CHAR_NSQ             ::= PRINTABLE_CHAR_NSQ  // No Single Quotes (NSQ)
                      |  ESCAPE_CHAR
CHAR_NDQ             ::= PRINTABLE_CHAR_NDQ  // No Double Quotes (NDQ)
                      |  ESCAPE_CHAR

NAME                 ::= "[a-zA-Z][a-zA-Z0-9_-]*"
INTEGER_LIT          ::= [0-9]+
FLOATING_POINT_LIT   ::= [0-9]* '.' [0-9]+
BOOLEAN_LIT          ::= 'true'
                      |  'false'
CHAR_LIT             ::= '\'' PRINTABLE_CHAR_NSQ '\''                     
STRING_LIT           ::= '"' CHARACTERS '"'

GAME_DEFN            ::= GAME_NAME '{' MEMBERS '}'
GAME_NAME            ::= NAME
MEMBERS              ::= MEMBER
                      |  MEMBER MEMBERS
MEMBER               ::= 'val' GAME_PART = PART_DEFN
PART_NAME            ::= 'players' 
                      |  'playStyle' 
                      |  'turnStyle'
                      |  'graph'
                      |  'legalMoves'
                      |  'pieces'
                      |  'endConditions'
                      |  'inputs'

PART_DEFN            ::= PLAYERS_DEFN
                      |  PLAYSTYLE_DEFN
                      |  TURNSTYLE_DEFN
                      |  GRAPH_DEFN
                      |  LEGALMOVES_DEFN
                      |  PIECES_DEFN
                      |  ENDCONDITIONS_DEFN
                      |  INPUTS_DEFN


PLAYSTYLE_DEFN       ::= 'Antagonistic'
                      |  'Oportunistic'
                      |  'Cooperative'

TURNSTYLE_DEFN       ::= 'Sequential'
                      |  'Simultaneous'
                      |  'Mixed'
                      |  'FixedTime'

// Comma separated (CS)
PLAYER_LIST_DEFN     ::= 'LIST' '(' CS_PLAYER_DEFN ')'
PLAYER_DEFN          ::= 'Player' '(' STRING_LIT ')'
                      |  'PreviousPlayer'
                      |  'CurrentPlayer'
                      |  'NextPlayer'
                      |  'AnyPlayer'
                      |  'AllPlayers'
                      |  'NoPlayer'
                      |  'NullPlayer'

CS_PLAYER_DEFN       ::= PLAYER_DEFN ',' CS_PLAYER_DEFN 
                      |  PLAYER_DEFN

NODES_DEFN           ::= 'LIST' '(' CS_NODE_DEFN ')'
NODE_DEFN            ::= 'PointNode' '(' '(' INT_LIT ',' INT_LIT ')' ')'
CS_NODE_DEFN         ::= NODE_DEFN ',' CS_NODE_DEFN 
                      |  NODE_DEFN

EDGES_DEFN           ::= 'LIST' '(' CS_EDGE_DEFN ')'
EDGE_DEFN            ::= 'edge' '(' NODE_DEFN ')'
CS_EDGE_DEFN         ::= EDGE_DEFN ',' CS_EDGE_DEFN 
                      |  EDGE_DEFN

GRAPH_DEFN           ::= 'Graph' '(' NODES_DEFN ',' EDGE_DEFN ')'
LEGALMOVES_DEFN      ::= 'LIST' '(' CS_LEGALMOVE_DEFN ')'
LEGALMOVE_DEFN       ::= LEGALMOVE '(' STRING_LIT ')'
CS_LEGALMOVE_DEFN    ::= LEGALMOVE_DEFN ',' CS_LEGALMOVE_DEFN 
                      |  LEGALMOVE_DEFN

PIECES_DEFN          ::= 'LIST' '(' CS_PIECE_DEFN ')'
PIECE_DEFN           ::= 'piece' '(' STRING_LIT ')'
CS_PIECE_DEFN        ::= PIECE_DEFN ',' CS_PIECE_DEFN 
                      |  PIECE_DEFN

ENDCONDITIONS_DEFN   ::= 'LIST' '(' CS_ENDCONDITION_DEFN ')'
ENDCONDITION_DEFN    ::= 'endCondition' '(' STRING_LIT ')'
CS_ENDCONDITION_DEFN ::= ENDCONDITION_DEFN ',' CS_ENDCONDITION_DEFN 
                      |  ENDCONDITION_DEFN

INPUTS_DEFN          ::= 'LIST' '(' CS_INPUT_DEFN ')'
INPUT_DEFN           ::= 'input' '(' STRING_LIT ')'
CS_INPUT_DEFN        ::= INPUT_DEFN ',' CS_INPUT_DEFN 
                      |  INPUT_DEFN

Action_DEFN          ::= 'Push'
                      |  'Pop'
                      |  'Place'

FUNCTION_DEFN        ::= scala function
GAME_OUTCOME         ::= 'Win'
                      |  'Lose'
                      |  'Draw'
                      |  'Pending'
