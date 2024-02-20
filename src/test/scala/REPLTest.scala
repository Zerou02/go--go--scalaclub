package ScalaGo

import ScalaGo.*
import ScalaGo.Color.*
import ScalaGo.FieldContent.*
import org.scalatest.flatspec.AnyFlatSpec

class REPLTest extends AnyFlatSpec {


  "A REPL" should "print a board Array(Array(Empty(), Stone(Black), Stone(White)), Array(Stone(Black), RemovedStone(White), Stone(Black)), Array(RemovedStone(Black), Stone(Black), Empty())) as '┌───┬───┬───┐\n│   │ ● │ ○ │ 0\n├───┼───┼───┤\n│ ● │ ◌ │ ● │ 1\n├───┼───┼───┤\n│ ◍ │ ● │   │ 2\n└───┴───┴───┘\n  0   1   2 '" in {
    var board: Goboard = Array(Array(Empty(Nobody), Stone(Black), Stone(White)),
      Array(Stone(Black), Empty(White), Stone(Black)),
      Array(Empty(Black), Stone(Black), Empty(Nobody)))
    var s1 = REPL.getFieldString(board, null)
    assert(s1.equals("┌───┬───┬───┐\n" + "│   │ ● │ ○ │ 0\n" + "├───┼───┼───┤\n" + "│ ● │ ◌ │ ● │ 1\n" + "├───┼───┼───┤\n" +
      "│ ◍ │ ● │   │ 2\n" + "└───┴───┴───┘\n" + "  0   1   2 "))
  }
  it should "print a field with a white stone as ○" in {
    assert(REPL.getFieldContentString(Stone(White)).equals("○"))
  }
  it should "print a field with a black stone as ●" in {
    assert(REPL.getFieldContentString(Stone(Black)).equals("●"))
  }
  it should "print a field with a removed white stone as ◌" in {
    assert(REPL.getFieldContentString(Empty(White)).equals("◌"))
  }
  it should "print a field with a removed black stone as ◍" in {
    assert(REPL.getFieldContentString(Empty(Black)).equals("◍"))
  }
  it should "print a empty field as a ' '" in {
    assert(REPL.getFieldContentString(Empty(Nobody)).equals(" "))
  }

}
