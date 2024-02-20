package ScalaGo

import SimpleList.*
import org.scalatest.flatspec.AnyFlatSpec


class SimpleListTest extends AnyFlatSpec {

  "A SimpleList" should "contain elements appended to it" in {
    var sl: SimpleList[Int] = new SimpleList[Int]()
    append(sl, 2)
    append(sl, 42)
    append(sl, 1337)
    assert(contains(sl, 2))
    assert(contains(sl, 42))
    assert(contains(sl, 1337))
  }
  it should "not contain elements not added" in {
    var sl: SimpleList[Int] = new SimpleList[Int]()
    assert(!contains(sl, 1))
    assert(!contains(sl, 4))
  }
  it should "have a length of 0 if no elements have been added" in {
    var sl: SimpleList[Int] = new SimpleList[Int]()
    assert(length(sl) == 0)
  }
  it should "have a length according to the number of elements appended to it" in {
    var sl: SimpleList[Int] = new SimpleList[Int]()
    append(sl, 2)
    append(sl, 42)
    append(sl, 1337)
    assert(length(sl) == 3)
  }

}
