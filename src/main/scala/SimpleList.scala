package ScalaGo

object SimpleList:

  /**
   * Implements a basic linked list.
   *
   * @param entry entry of the list element, 'None' if not provided
   * @param next  next element of the list, 'null' if not provided
   * @tparam A type of the list's entries
   */
  class SimpleList[A](var entry: Option[A] = None, var next: SimpleList[A] = null)

  def getLast[A](list: SimpleList[A]):SimpleList[A] = {
    var current = list;
    while (current.next != null){
        current = current.next;
    }
    return current;
  }
  /**
   * Append an item to the SimpleList
   *
   * @param list the list
   * @param item append this item
   */
  def append[A](list: SimpleList[A], item: A): Unit = {
    var last = getLast(list);
    if(last.entry.isEmpty){
          list.entry = Some(item);
    }else{
        last.next = SimpleList(Some(item),null);
    }
  }

  /**
   * Determines the length of the SimpleList.
   *
   * @param list the list
   * @return the length
   */
  def length[A](list: SimpleList[A]): Int = {
    if(list == null || list.entry.isEmpty){return 0;}
    var current = list;
    var amount = 1;
    while (current.next != null ){
        current = current.next;
        amount+=1;
    }
    return amount;
  }

  /**
   * Tests if the SimpleList contains a given element.
   *
   * Tests for equality are done via '=='.
   *
   * @param list the list
   * @param item search for this item
   * @return true if the SimpleList contains the item, false if not
   */
  def contains[A](list: SimpleList[A], item: A): Boolean = {
    var found = false;
    var current = list;
    if(current.entry.isDefined){
        found = current.entry.get == item;
    }
    while ((current.next != null)){
        if(current.entry.isDefined){
            println(current.entry)
            if(current.entry.get == item){
                found = true;
            }
        }
        current = current.next;
    }
    return found;
  }