static boolean evaluatesToLocalValue(Node value, Predicate<Node> locals) {
  switch (value.getType()) {
    // ... [other cases remain unchanged] ...
    case Token.NEW:
      // Check if the new object is local by ensuring it has not been aliased from inside the constructor.
      return !isAliasedWithinConstructor(value);
    // ... [rest of the cases remain unchanged] ...
  }
}

// Additional method to check if an object was aliased within its constructor
static boolean isAliasedWithinConstructor(Node newValue) {
  Node parent = newValue.getParent();
  while (parent != null && parent.getType() == Token.FUNCTION) { // Check for constructors
    Node firstChild = parent.getFirstChild();
    while (firstChild != null) {
      if (isAssignmentOp(firstChild) && firstChild.getLastChild().equals(newValue)) {
        return true; // The new object has been assigned, thus it is not local.
      }
      firstChild = firstChild.getNextSibling(); // Move to the next sibling
    }
    parent = parent.getNextSibling(); // Move to the next function node (if any)
  }
  return false; // No aliasing found within constructor, so it is local.
}

// Helper method to check if an operation is an assignment
static boolean isAssignmentOp(Node node) {
  switch (node.getType()) {
    case Token.ASSIGN:
    case Token.COMMA: // Consider comma as an assignment in this context
      return true;
    default:
      return false;
  }
}