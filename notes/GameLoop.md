# State machine

## States

S: Start state
I: Initialization
Set: Setup
Tn: Turn n
PnTn: Phase N of Turn N
WfI: Wait for input
NT: Next turn
NP: Next phase
ECc: EndCondition check
Fin: Game finished

## Input alphabet

i: ignore
e: empty
Pn: Max number of players
Pj: Player joins
nMP: nMP
GST: GameState transformation


## Stack alphabet

i: ignore
e: empty (pop symbol)
X: number
nP: number of turn phases


## Transition relations
{
    currentState,
    inputCharacter,
    topSymbol,
    stateTransition,
    stackTransition (or function)
}


{S, i,  I,   i, Pn}

Wait for players to join
{I, Pj, X,   I, X - 1}
{I, Pj, 0,   I, e}
{I, Pj, nMP, I, e}

Perform setup actions
{Set_0, i, i, Set0, GST}
{Set_1, i, i, Tn,   0}


### Initialization

Wait for players

### Setup

Perform actions that take place before the first turn.



