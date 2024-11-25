private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);
  if (ns != null) {
    boolean isLastChildOfScopeNode = false;
    
    // Check if this assignment is the last child of a scope node (e.g., FOR loop)
    if (parent.getType() == Token.FOR) {
      isLastChildOfScopeNode = parent.getLastChild() == n;
    }

    if ((isLastChildOfScopeNode && !NodeUtil.isForIn(parent)) || 
        (parent.getType() != Token.FOR && parent.getType() != Token.IF && parent.getType() != Token.WHILE) ||
        (parent.getType() == Token.FOR && NodeUtil.isForIn(parent))) {
      recordDepScope(recordNode, ns);
    } else {
      if (n.getType() == Token.ASSIGN) {
        // Record the dependency scope for the right-hand side of the assignment
        recordConsumers(t, n.getLastChild(), nameNode); // Assuming getLastChild is the RHS of ASSIGN
      }
      
      // Record the scope for the left-hand side of the assignment
      if (n.getType() == Token.ASSIGN) {
        Node lhs = n.getFirstChild();
        if (lhs != null && lhs.getType() != Token.THIS) { // This check avoids recording 'this' as a dependency scope
          recordDepScope(lhs, ns);
        }
      }
    }
  }
}