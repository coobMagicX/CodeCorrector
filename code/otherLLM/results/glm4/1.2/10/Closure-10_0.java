// Assuming the following methods are available:
// Node class definition and relevant fields (e.g., type)
// static boolean allResultsMatch(Node n, Predicate<Node> predicate);
// static boolean mayBeStringHelper(Node n);

// Also assuming there is a constant for the string type identifier or value
// e.g. final int STRING_TYPE = ...;

static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    return allResultsMatch(n, node -> node.getType() == STRING_TYPE || 
                             (node.hasChildren() && mayBeStringHelper(node)));
  } else {
    return mayBeStringHelper(n);
  }
}

// Now the `mayBeString` function checks for string type directly and recursively