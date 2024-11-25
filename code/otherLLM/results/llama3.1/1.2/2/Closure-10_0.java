static boolean mayBeString(Node n, boolean recurse) {
  if (recurse && isLiteralExpression(n)) { // Check if the expression is a literal
    return true; // If it's a literal, it can be a string
  }

  if (recurse) {
    return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    return mayBeStringHelper(n);
  }
}

// Helper method to check if the expression is a literal
static boolean isLiteralExpression(Node n) {
  // Check if the node type is one of the literal types (e.g., Token.STRING, Token.NUMBER)
  return n.getType() == Token.STRING || 
         n.getType() == Token.NUMBER || 
         n.getType() == Token.TRUE || 
         n.getType() == Token.FALSE;
}