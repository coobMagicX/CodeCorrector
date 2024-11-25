private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);

  if (ns != null) {
    // Determine the dependency scope for assignments inside a FOR loop
    boolean isForLoopHeader = false;
    if (parent.isFor() && !NodeUtil.isForIn(parent)) {
      Node firstChild = parent.getFirstChild();
      isForLoopHeader = firstChild.getNext() == n;
      
      // Record dependency scope for assignments in the init, condition or iteration part of a FOR loop
      if (!isForLoopHeader) {
        recordDepScope(recordNode, ns); // Record at the assignment node if not the header
      } else {
        recordDepScope(nameNode, ns); // Record at the name node if it's the header
      }
    } else {
      // For assignments outside of a FOR loop or where parent is a call expression (e.g., x.f = y)
      boolean isCallExpression = parent.getType() == Node.Token.CALL;
      
      if (!isCallExpression) {
        recordDepScope(recordNode, ns); // Record at the assignment node
      } else {
        // For assignments within a call expression, don't record scope for the lhs of the assignment
        // since it's used by the context and not associated with a specific scope in this case.
      }
    }
  }
}

// Method that can be used in the context:
private void recordDepScope(Node node, NameInformation name) {
  Preconditions.checkNotNull(name);
  scopes.put(node, name);
}