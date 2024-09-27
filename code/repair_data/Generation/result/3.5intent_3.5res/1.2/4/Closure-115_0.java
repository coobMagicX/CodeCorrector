boolean isDirectCallNodeReplacementPossible(Node fnNode) {
  // Only inline single-statement functions
  Node block = NodeUtil.getFunctionBody(fnNode);

  // Check if this function is suitable for direct replacement of a CALL node:
  // a function that consists of single return that returns an expression.
  if (!block.hasChildren()) {
    // special case empty functions.
    return true;
  } else if (block.hasOneChild()) {
    Node stmt = block.getFirstChild();

    // Only inline functions that return something.
    if (stmt.isReturn() && stmt.getFirstChild() != null) {
      // Check if the return statement has any side effects.
      if (!NodeUtil.mayHaveSideEffects(stmt.getFirstChild(), compiler)) {
        return true;
      }
    }
  }

  return false;
}