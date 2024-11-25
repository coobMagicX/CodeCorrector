static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    return mayBeStringHelper(foldSame(n));
  }
}

// Modified foldSame function to handle ternary expressions and bitwise OR operations
static Node foldSame(Node n) {
  if (n.getType() == Token.TERNARY) {
    // Handle ternary expression by folding the three child nodes together
    return new TernaryNode(foldSame(n.getFirstChild()), foldSame(n.getMiddleChild()), foldSame(n.getLastChild()));
  } else if (n.getType() == Token.BITOR) {
    // Handle bitwise OR operation by folding the two child nodes together
    return new BitwiseORNode(foldSame(n.getFirstChild()), foldSame(n.getLastChild()));
  } else {
    // Default to existing implementation for other node types
    return super.foldSame(n);
  }
}