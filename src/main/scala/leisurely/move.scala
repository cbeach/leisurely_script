package org.leisurelyscript

import MoveAction._

case class Move(piece:Piece, player:Player, action:MoveAction, node:BoardNode) {}
