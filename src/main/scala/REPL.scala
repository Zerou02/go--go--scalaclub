package ScalaGo

import ScalaGo.*
import ScalaGo.Color.*
import ScalaGo.FieldContent.*

import scala.io.StdIn
import SimpleList.*

object REPL:

  /**
    * This function runs and evaluates game states and transitions
    * until the global variable gs reaches the passed end state
    *
    * @param endState run until this state is reached
    */
  def repl(endState: GameState): Unit =
    while gs != endState
    do
        ???

  /**
    * Return a String depicting the content of the field
    *
    * @param c the FieldContent to depict
    * @return String depicting the FieldContent
    */
  def getFieldContentString(c: FieldContent): String =
    ???

  /**
    * Return a String displaying the current state of the board. If the previous board is supplied it is used to
    * determine differences between the boards to highlight them.
    *
    * @param b board to return as string
    * @param prev previous board to highlight differences
    * @return String representation of the current board
    */
  def getFieldString(b: Goboard, prev: Goboard): String =
    var boardBuilder = new StringBuilder()

    ???

    return boardBuilder.toString()

  /**
    * Print the board to the standard output
    *
    * @param b current board
    * @param pB previous board, may be used to detect and higlight differences
    */
  def printBoard(b: Goboard, pB: Goboard): Unit =
    println(getFieldString(b, pB))

  /**
    * Print which player's turn it is to standard output
    */
  def printCurrentPlayer(): Unit =
    ???

  /**
    * Print the players' score to standard output
    *
    * @param stonesWhite number of white stones on the board
    * @param fieldsWhite number of fields enclosed by white stones
    * @param stonesBlack number of black stones on the board
    * @param fieldsBlack number of fields enclosed by black stones
    */
  def printFinalScore(stonesWhite: Int, fieldsWhite: Int, stonesBlack: Int, fieldsBlack: Int): Unit =
    ???
