private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild(); // Assuming this represents the lhs (left-hand side) of the assignment
  Node parent = n.getParent();
  Node rhsNode = nameNode.getNext(); // The right-hand side of the assignment
  NameInformation ns = createNameInformation(t, nameNode);

  if (ns != null) {
    // Check if the parent is a FOR loop and special-case handle "for-in" loops
    if (parent.isFor() && !NodeUtil.isForIn(parent)) {
      // Correct scoping for variables within FOR loops to avoid misattributed dependencies
      if (parent.getFirstChild() == nameNode) {
        recordDepScope(nameNode, ns);
      } else {
        recordDepScope(recordNode, ns);
      }
    } else {
      // Default case: Check whether this assignment is part of a larger expression
      if (isPartOfExpression(parent)) {
        // Handle right-hand side only if it's part of an expression (not a simple assignment)
        recordDepScope(rhsNode, ns);
      } else {
        // Normal assignment that is not part of a higher order composite expression
        recordDepScope(recordNode, ns);
      }
    }
  }
}

private boolean isPartOfExpression(Node parent) {
  // Returns true if the parent node indicates this assignment is part of a larger expression
  return parent.isExprResult() || parent.isReturn() || parent.isCall();
}
