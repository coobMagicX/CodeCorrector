static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    // directly check the node
    if (n.isAdd()) { // Addition could result in a string due to type coercion if any operand can be a string.
      Node left = n.getFirstChild();
      Node right = left.getNext();
      return mayBeString(left, false) || mayBeString(right, false);
    } else {
      return mayBeStringHelper(n);
    }
