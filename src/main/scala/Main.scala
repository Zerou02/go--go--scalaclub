object Main extends App {

  import java.util.Calendar
  import javax.swing.text.Position
  import scala.compiletime.ops.int
  import scala.languageFeature.postfixOps
  import java.awt.Color
  import javax.sound.sampled.AudioSystem
  import java.io.File

  type Board = Array[Array[Option[Stone]]]

  enum Colour {
    case Black, White;
  }

  class Stone(var colour: Colour)
  class Config(var size: Int);
  class State(var currTurn: Int = 0, var passed: Int = 0);

  enum AnsiCodes(val code: String) {
    case ResetColour extends AnsiCodes("\u001b[0m")
    case White extends AnsiCodes("\u001b[97m")
    case Noise extends AnsiCodes("\u001b[0m")
    case Gray extends AnsiCodes("\u001b[90m")
    case Green extends AnsiCodes("\u001b[32m")
    case Black extends AnsiCodes("\u001b[30m")
    case Red extends AnsiCodes("\u001b[31m")
    case Clear extends AnsiCodes("\u001b[2J")
    case RedBG extends AnsiCodes("\u001b[101m")
    case YellowBG extends AnsiCodes("\u001b[103m")
    case PurpleBG extends AnsiCodes("\u001b[105m")
    case GreenBG extends AnsiCodes("\u001b[42m")
    case BlueBG extends AnsiCodes("\u001b[44m")
  }

  val backgroundColours = Array(
    AnsiCodes.RedBG.code,
    AnsiCodes.YellowBG.code,
    AnsiCodes.PurpleBG.code,
    AnsiCodes.GreenBG.code,
    AnsiCodes.BlueBG.code
  );

  val playerNames = Array("Schwarz", "Weiß");
  var players = Array(Colour.Black, Colour.White);

  var config = new Config(3);
  var board = createBoard(config);
  var state = new State();

  var messageBuffer = List("");
  var inDanceMode = false;
  var frameCount = 0;
  var passed = 0L;
  var lengthCaramell = 60;

  board(0)(0) = Some(Stone(Colour.White))
  board(1)(1) = Some(Stone(Colour.Black))

  // startGame();
  var test = new Array[Array[Int]](3);
  println(test(0)(0))

  def loop(onEnd: () => Unit) = {

    var stream = AudioSystem.getAudioInputStream(
      new File(
        "./caramell.wav"
      )
    );
    var clip = AudioSystem.getClip();
    clip.open(stream);
    clip.start();

    val c = Calendar.getInstance();
    var start = System.nanoTime();
    var frameThreshold = 1f / 60f * 1e9f;
    while (inDanceMode && frameCount <= 60 * lengthCaramell) {
      var newMillis = System.nanoTime();
      var passed = newMillis - start;
      if (passed >= frameThreshold) {
        clearConsole();
        if (frameCount >= 1020) {
          printCaramelBoard(board, frameCount)
        } else {
          printBoard(board);
        }
        // drawBall(frameCount);
        println(
          "Frames:" + frameCount.toString() + "/" + (lengthCaramell * 60)
            .toString() + "," + "Since last frame: " + passed / 1e9
            .floatValue() + "FPS:" + 1 / (passed / 1e9)
        )
        //      startGame();

        passed = 0;
        start = newMillis;
        frameCount += 1;
      }
    }
    inDanceMode = false;
    clip.close();
    onEnd();

  }

  def dance() = {
    inDanceMode = true;
    passed = 0;
    frameCount = 0;
    loop(() => { handleOptions() });
  }

  def colourBGRand(frame: Int, sb: java.lang.StringBuilder) = {
    sb.append(backgroundColours((frame / 5) % backgroundColours.length));
    // sb.append(AnsiCodes.ResetColour.code);
  }
  def drawBall(frame: Int) = {
    var sb = new java.lang.StringBuilder();
    for (j <- 5 to 15) {
      for (i <- 0 until 20) {
        colourBGRand(frame, sb)
      }
      sb.append("\n");
    }
    println(sb.toString());
  }

  def handleConfigCreation() = {
    clearConsole();
    println("Wie groß soll das Brett sein?");
    var x = getSanitizedInt("Größe:");
    config = new Config(x);
  }
  def startGame() = {
    clearConsole();
    handleConfigCreation();
    println("-----Go!  Go!  Scalaclub!-----")
    board = createBoard(config);
    state = new State();
    printBoard(board);
    handleTurnBegin();
  }

  def handleShowBoard(): Unit = {
    clearConsole();
    printBoard(board);
    println((playerNames(state.currTurn)) + " ist am Zug")
    handleOptions();
  }

  def clearConsole() = {
    println(AnsiCodes.Clear.code);
  }

  def handleEnd() = {
    clearConsole();
    var points = calcPoints(board);
    println("ENTE!");
    println("Weiß hat " + points(0) + " Punkte.");
    println("Schwarz hat " + points(1) + " Punkte.\n");
    println("Noch eine Runde?");
    println("(1): Ja");
    println("(2): Nein");
    var option = getSanitizedInt();
    if (option == 1) {
      startGame();
    }

  }

  def calcPoints(board: Board): Array[Int] = {
    var visitedArr = Array.fill(board.length, board(0).length)(false);
    var points = Array(0, 0); // white, black
    board.zipWithIndex.foreach((y, i) => {
      y.zipWithIndex.foreach((x, j) => {
        if (!board(i)(j).isDefined && !visitedArr(i)(j)) {
          var (positions, colour) = floodFill(board, new Position(j, i));
          if (colour.isDefined && colour.get == Colour.White) {
            points(0) += positions.length;
          } else if (colour.isDefined && colour.get == Colour.Black) {
            points(1) += positions.length;
          }
          positions.foreach(z => {
            visitedArr(z.y)(z.x) = true;
          })
        };
      })
    });
    return points;
  }

  def handleEndOfTurn() = {
    state.currTurn = (state.currTurn + 1) % playerNames.length;
    clearConsole();
    handleTurnBegin();
  }

  def handlePass() = {
    state.passed += 1;
    if (state.passed == 2) {
      handleEnd();
    } else {
      handleEndOfTurn()
    }
  }

  def handleTurnBegin() = {
    printBoard(board)
    messageBuffer.foreach(x => print(x));
    println((playerNames(state.currTurn)) + " ist am Zug!. Was willst du tun?")
    handleOptions();
  }

  def handleOptions(): Unit = {
    println("(1): " + "Brett anzeigen");
    println("(2): " + "Passen");
    println("(3): " + "Stein platzieren");
    println("(???): " + "Go!Go!")
    var option = getSanitizedInt();
    if (option == 1) {
      handleShowBoard();
    } else if (option == 2) {
      handlePass();
    } else if (option == 3) {
      handleStonePlacement();
    } else if (option == 4) {
      dance();
    }
  }

  class Position(var x: Int, var y: Int);

  def isPosInList(list: List[Position], pos: Position): Boolean = {
    return !list.forall(x => !(pos.x == x.x && pos.y == x.y))
  }

  def findList(
      board: Board,
      startPos: Position
  ): List[Position] = {
    var retList: List[Position] = List();
    var closedQueue: List[Position] = List()
    var colour = board(startPos.y)(startPos.x).get.colour;
    var queue = List(startPos);
    while (queue.length != 0) {
      var first = queue.head;
      findNeighbours(board, first).foreach(x => {
        if (
          board(x.y)(x.x).isEmpty || board(x.y)(
            x.x
          ).get.colour != colour
        ) {
          // continue
        } else {
          if (!isPosInList(closedQueue, x) && !isPosInList(queue, x)) {
            queue = queue.appended(x);
          }
        }
      });
      queue = queue.tail;
      retList = retList.appended(first);
      closedQueue = closedQueue.appended(first);
    }
    return retList;
  };

  def findNeighbours[T](arr: Array[Array[T]], pos: Position): List[Position] = {
    var queue: List[Position] = List();
    var vectors = List((0, -1), (1, 0), (0, 1), (-1, 0));
    vectors.foreach(x =>
      var newPos = new Position(pos.x + x(0), pos.y + x(1));
      if (
        newPos.x < 0 || newPos.y < 0 || newPos.x >= board(
          0
        ).length || newPos.y >= board(
          1
        ).length
      ) {
        // continue
      } else {
        queue = queue.appended(newPos);
      }
    )
    return queue;
  };

  def handleStoneDeletion(colour: Colour) = {
    var visitedArr = Array.fill(board.length, board(0).length)(false);
    var queue: List[Position] = List();
    var lists: List[List[Position]] = List();

    board.zipWithIndex.foreach((y, i) => {
      board.zipWithIndex.foreach((x, j) => {
        if (
          board(i)(j) != None && !visitedArr(i)(j) && board(i)(
            j
          ).get.colour == colour
        ) {
          visitedArr(i)(j) = true;
          var colour = board(i)(j).get.colour;
          var list = findList(board, new Position(j, i));
          list.foreach(z => {
            visitedArr(z.y)(z.x) = true;
          })
          lists = lists.appended(list);
        }
      });
    });
    var removeList: List[List[Position]] = List();
    println(colour)
    lists.foreach(x => {
      if (getLiberty(board, x) == 0) {
        removeList = removeList.appended(x);
      }
    })
    println("remove:" + removeList.length);
    removeList.foreach(x => {
      var playerName = "Weiß";
      if (colour == Colour.Black) {
        playerName = "Black";
      }
      messageBuffer = messageBuffer.appended(
        "Es wurde eine Reihe von " + playerName + " entfernt. \n"
      )
      x.foreach(y => {
        board(y.y)(y.x) = None
      })
    })
  }

  def getLiberty(board: Board, list: List[Position]): Int = {
    var highestLiberty = -1;
    list.foreach(x => {
      var stoneLib = 0;
      findNeighbours(board, x).foreach(y => {
        if (
          y.x < 0 || y.y < 0 || y.x >= board(
            0
          ).length || y.y >= board.length || board(y.y)(y.x) != None
        ) {} else {
          stoneLib += 1;
        }
      })
      highestLiberty = Math.max(stoneLib, highestLiberty);
    })
    return highestLiberty;
  }

  def getSanitizedInt(preface: String = ""): Int = {
    var retVal = -1;
    var gotInt = false;
    while (!gotInt) {
      print(preface);
      var line = scala.io.StdIn.readLine();
      try {
        retVal = line.toInt;
        gotInt = true;
      } catch { _ =>
        println("Zahl erwartet.Bitte korrekte Eingabe tätigen.")
      };
    }
    return retVal;
  }

  def handleStonePlacement() = {
    state.passed = 0;
    clearConsole();
    printBoard(board);
    var validPos = false;
    var x = -1;
    var y = -1;
    var firstTry = true
    while (!validPos) {
      if (firstTry) {
        println("Wo willst du den Stein platzieren?");
      }
      while (x < 0 || x >= board(0).length) {
        x = getSanitizedInt("x: ");
        if (x < 0 || x >= board(0).length) {
          println("Position ungültig. Bitte neu eingeben.")
        }
      }
      while (y < 0 || y >= board.length) {
        y = getSanitizedInt("y: ");
        if (y < 0 || y >= board.length) {
          println("Position ungültig. Bitte neu eingeben.")
        }
      }
      if (board(y)(x).isDefined) {
        println("Belegt!! Bitte neue Position wählen");
        x = -1;
        y = -1;
        firstTry = false;
      } else {
        validPos = true;
      }
    }
    var colour = Colour.Black;
    if (playerNames(state.currTurn) == "Weiß") {
      colour = Colour.White;
    }
    board(y)(x) = Some(Stone(colour))
    handleStoneDeletionBoth();
    handleEndOfTurn();
  }

//Might be standard method for enclosed space
  def floodFill(
      board: Board,
      startPos: Position
  ): (List[Position], Option[Colour]) = {
    var visited: List[Position] = List();
    var open = List(startPos);
    var closed: List[Position] = List();
    var retList: List[Position] = List();
    var whiteEncountered = false;
    var blackEncountered = false;
    while (open.length != 0) {
      var first = open.head;
      open = open.tail;
      closed = closed.appended(first);
      findNeighbours(board, first).foreach(x => {
        if (!isPosInList(closed, x) && !isPosInList(open, x)) {
          if (board(x.y)(x.x).isDefined) {
            var stone = board(x.y)(x.x).get;
            if (stone.colour == Colour.White) {
              whiteEncountered = true;
            } else {
              blackEncountered = true;
            }
          } else {
            open = open.appended(x);
          }
        }
      });
      retList = retList.appended(first)
    }
    if (blackEncountered && whiteEncountered) {
      return (List(), None);
    } else {
      if (blackEncountered) {

        return (retList, Some(Colour.Black));
      } else {
        return (retList, Some(Colour.White));

      }
    }
  }

  def handleStoneDeletionBoth() = {
    handleStoneDeletion(players((state.currTurn + 1) % players.length));
    handleStoneDeletion(players(state.currTurn));
  }

  def createBoard(config: Config): Board = {
    return Array.fill(config.size, config.size)(None);
  }

  def printBoard(board: Board) = {
    var builder = new StringBuilder();
    for (j <- 0 until board(0).length * 2 + 1) {
      builder.append(AnsiCodes.Green.code);
      builder.append("-");
      builder.append(AnsiCodes.ResetColour.code);
    }
    builder.append("\n");
    board.zipWithIndex.foreach((y, i) => {
      y.zipWithIndex.foreach((x, k) => {
        builder.append(AnsiCodes.Green.code);
        builder.append("|");
        builder.append(AnsiCodes.ResetColour.code);
        if (board(i)(k).isDefined) {
          if (board(i)(k).get.colour == Colour.White) {
            builder.append(AnsiCodes.White.code);
          } else {
            builder.append(AnsiCodes.Red.code);
          }
          builder.append(".");
          builder.append(AnsiCodes.ResetColour.code);
        } else {
          builder.append(" ")
        }

      });
      builder.append(AnsiCodes.Green.code);
      builder.append("|");
      builder.append(AnsiCodes.ResetColour.code);
      builder.append("\n");
      for (j <- 0 until board(0).length * 2 + 1) {
        builder.append(AnsiCodes.Green.code);
        builder.append("-");
        builder.append(AnsiCodes.ResetColour.code);
      }
      builder.append("\n");

    })
    println(builder.toString());
  }

  def printCaramelBoard(board: Board, frame: Int) = {
    var builder = new java.lang.StringBuilder();
    for (j <- 0 until board(0).length * 2 + 1) {
      builder.append(AnsiCodes.Green.code);
      colourBGRand(frame, builder);
      builder.append("-");
      builder.append(AnsiCodes.ResetColour.code);
    }
    builder.append("\n");
    board.zipWithIndex.foreach((y, i) => {
      y.zipWithIndex.foreach((x, k) => {
        builder.append(AnsiCodes.Green.code);
        colourBGRand(frame, builder);
        builder.append("|");
        builder.append(AnsiCodes.ResetColour.code);
        if (board(i)(k).isDefined) {
          if (board(i)(k).get.colour == Colour.White) {
            builder.append(AnsiCodes.White.code);
          } else {
            builder.append(AnsiCodes.Red.code);
          }
          colourBGRand(frame, builder);

          builder.append(".");
          builder.append(AnsiCodes.ResetColour.code);
        } else {
          colourBGRand(frame, builder);

          builder.append(" ")
          builder.append(AnsiCodes.ResetColour.code);
        }

      });
      builder.append(AnsiCodes.Green.code);
      colourBGRand(frame, builder);

      builder.append("|");
      builder.append(AnsiCodes.ResetColour.code);
      builder.append("\n");
      for (j <- 0 until board(0).length * 2 + 1) {
        builder.append(AnsiCodes.Green.code);
        colourBGRand(frame, builder);
        builder.append("-");
        builder.append(AnsiCodes.ResetColour.code);
      }
      builder.append("\n");

    })
    println(builder.toString());
  }
}
