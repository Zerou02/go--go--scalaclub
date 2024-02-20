package ScalaGo

import SimpleList.*

object ScalaGo:
  

  /**
    * Type for specifying a stone's or field's position
    */
  type Position = (Int, Int)

  /**
    * Color of a player or stone
    */
  enum Color:
    case Black, White, Nobody

  import Color.*

  /**
    * FieldContent models the different fields the game board can have.
    */
  enum FieldContent (color: Color):

    /**
     * A stone of a specific color.
     *
     * @param color color of the stone
     */
    case Stone (color: Color) extends FieldContent (color: Color)

    /**
     * An empty field.
     * Has another color than Nobody if a stone was removed from this field in the last round; Referred to as RemovedStone in this case
     *
     * @param color color the stone belonged if a stone was removed from this field in the last round; Nobody if there was no stone
     */
    case Empty (color: Color) extends FieldContent (color)

  import ScalaGo.FieldContent.*


  /**
    * The game state controls the progression of the game.
    *
    * The transitions array contains tuples of functions and target states.
    * If the function of a tuple evaluates to true the target state is activated.
    *
    * @param player who's turn it is
    * @param transitions array of a tuple of transition functions and next GameState
    * @param startAction function to execute when entering the state
    */
  case class GameState(player: Color, transitions: Array[(String => Boolean, GameState)], startAction: () => Unit)


  /**
    * Chain of stones
    *
    * @param color color of the stones
    * @param position positions of the stones
    * @param liberties positions of the chain's liberties
    */
  case class Chain(color: Color, position: SimpleList[Position], liberties: SimpleList[Position])


  // Custom exceptions
  case class OutOfBoardException(msg: String) extends Exception

  case class InvalidBordsizeException(msg: String) extends Exception

  case class InvalidMoveException(msg: String = "This move is not possible!") extends Exception


  /**
    * Alias type for the game board
    */
  type Goboard = Array[Array[FieldContent]]

  var board: Goboard = null // The current board
  var prevBoard: Goboard = null // The previous board (used for detecting repeating positions)
  var prevPrevBoard: Goboard = null // The board before the last board (used for detecting repeating positions)

  var gs: GameState = null // Starting GameState, this global variable is used throughout the implementation

  //def main(args: Array[String]): Unit =

    ???
    //val (start, end) = buildStartToEndStates()

    //gs = start

    //REPL.repl(end)


  /**
    * Generate all necessary states with transitions for the game
    *
    * @return a tuple of the start and the end states
    */
  def buildStartToEndStates(): (GameState, GameState) =
    ???

  /**
    * Output the current board an player and afterwards remove old RemovedStone markers
    */
  def outputBoardAndPlayer(): Unit =
    ???

  /**
    * Remove all RemovedStone markers from the board
    *
    * @param b board to remove from
    */
  def removeOldRemovedStones(b: Goboard): Unit =
    ???

  /**
    * Remove stones of each chain's positions where the chain has no liberties
    *
    * @param chains list of chains
    * @param c      only remove stones of this color
    * @param b      board to remove the stones from
    */
  def removeStonesOfZeroChains(chains: SimpleList[Chain], c: Color, b: Goboard): Unit =
    ???

  /**
    * Remove all stones of chains with zero liberties starting with the opposing players color and then remove remaining
    * chains with zero liberties of the own color
    *
    * @param b board to remove the stones from
    */
  def killChains(b: Goboard): Unit =
    ???

  /**
    * Tests if two boards are identical with regard to only Empty and Stones.
    *
    * @param b1 board one
    * @param b2 board two
    * @return true if both boards represent equal positions, false otherwise
    */
  def equalBoardPositions(b1: Goboard, b2: Goboard): Boolean =
    ???

  /**
    * Create a real copy of the board
    *
    * @param b board to copy
    * @return an identical board at a different memory location
    */
  def copyBoard(b: Goboard): Goboard =
    ???

  /**
    * Set a position within a board. Thows an OutOfBoardException if the position is not within the board.
    *
    * @param b   the board to place the field in
    * @param pos the position
    * @param fc  the new content of the field at position
    */
  def setPosition(b: Goboard, pos: Position, fc: FieldContent): Unit =
    ???

  /**
    * Test if a field is only surrounded by stones of one color. This search may span multiple Empty fields.
    *
    * @param pos start position
    * @param c   color to match
    * @param b   board to search
    * @return true if the field is only surrounded by stones of the given color, false otherwise
    */
  def isSurroundedByOnly(pos: Position, c: Color, b: Goboard): Boolean =
    ???

  /**
    * Calculate the score according to the area covered by the players
    *
    */
  def calculateScore(): Unit =
    ???

  /**
    * Generate a board of size*size dimensions.
    *
    * @param size the horizontal and vertical dimension of the board
    * @return the board initialized with Empty() fields
    */
  def buildBoardTransition(size: String): Boolean =
    ???

  /**
    * Parse a player's input to detect a stone placement command and place a stone
    *
    * @param command input from the player
    * @return true if the command could be understood, false otherwise
    */
  def placeStoneTransition(command: String): Boolean =
    ???

  /**
    * Parse a player's input to detect the 'pass' command
    *
    * @param command input from the player
    * @return true if the command could be understood, false otherwise
    */
  def passTransition(command: String): Boolean =
    if command.equals("pass") then return true
    else return false

  /**
    * Get the contents of a field by row,column indices.
    *
    * This is a convenience function that directly calls getFieldConten(p: Position, b: Goboard)
    *
    * @param row    row index
    * @param column column index
    * @param b      the board
    * @return the FieldContent which may be OutOfBounds
    */
  def getFieldContent(row: Int, column: Int, b: Goboard): FieldContent = getFieldContent((row, column), b)

  /**
    * Get the contents of a field by Position.
    *
    * @param p the position
    * @param b the board
    * @return the FieldContent which may be OutOfBounds
    */
  def getFieldContent(p: Position, b: Goboard): FieldContent =
    ???

  /**
    * Used with the exploreChain function to add stones' position to the chain
    *
    * @param chain chain to extend
    * @param pos position to check
    * @param b board to use
    */
  def addAdjacentStones(chain: Chain, pos: Position, b: Goboard): Unit = ???

  /**
    * Explore all positions of a chain and apply passed function to all fields surrounding the chain's positions
    *
    * @param chain chain to explore
    * @param apply apply this function to all fields surrounding the chain's positions
    * @param b the board to use while exploring the chain
    */
  def exploreChain(chain: Chain, apply: (Chain, Position, Goboard) => Unit, b: Goboard): Unit =
    ???

  /**
    * Find all chains on the board
    *
    * @param b board to search for chains
    * @return list of chains
    */
  def findChains(b: Goboard): SimpleList[Chain] =
    ???

  /**
    * Used with the exploreChain function to add the positions of liberties to the chain
    *
    * @param chain chain to add the liberties to
    * @param pos position to search for a libertie
    * @param b board to use
    */
  def addLiberties(chain: Chain, pos: Position, b: Goboard): Unit = ???
  /**
    * Find all positions that are liberties along a chain
    *
    * @param chains chain to search for libertie positions
    * @param b board to use
    */
  def updateLiberties(chains: SimpleList[Chain], b: Goboard): Unit =
    ???

  /**
    * Check if the position is already a position of the chain
    *
    * @param pos position to check
    * @param chains chain to search
    * @return true if the position is already in the chain's position list
    */
  def isPositionInChains(pos: Position, chains: SimpleList[Chain]): Boolean =
    ???
