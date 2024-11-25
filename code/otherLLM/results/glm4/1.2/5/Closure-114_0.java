private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);
  if (ns != null) {
    // Check for FOR loop assignments and correct dependency scope
    if (parent.isFor() && !NodeUtil.isForIn(parent)) {
      // Determine the correct node to record dependency scope based on the position in the FOR loop
      Node prevSibling = parent.getFirstChild();
      while (prevSibling.getNext() != n) {  // Traverse siblings until reaching the current node
        prevSibling = prevSibling.getNext();
      }
      
      if (parent.getFirstChild().getNext() == n) {
        // The assignment is the last element in the init or condition part of the FOR loop
        recordDepScope(recordNode, ns);
      } else if (prevSibling.getNext() == parent.getLastChild()) {
        // The assignment is in the iteration part of the FOR loop
        recordDepScope(recordNode, ns);
      } else {
        // The assignment is in the init or condition part but not the last element
        recordDepScope(nameNode, ns);  // Use the name node for dependency scope recording
      }
    } else {
      // Handle assignments where the right-hand side (rhs) of the assignment is the caller expression
      if (!n.isCallExpression()) {
        // If it's not a call expression, use the recordNode to associate with the context
        recordDepScope(recordNode, ns);
      }
    }
  }
}