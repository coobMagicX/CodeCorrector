private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);
  if (ns != null) {
    // Check if the assignment is part of a for loop's init, condition, or iteration
    boolean isInForLoopPart = false;
    if (parent.isFor() && !NodeUtil.isForIn(parent)) {
      Node firstChildOfParent = parent.getFirstChild();
      // If it is not the first child, then it could be in one of the for loop parts
      if (firstChildOfParent != n) {
        isInForLoopPart = true;
      }
    }

    // Record dependency scope based on whether it's a for loop part or not
    if (isInForLoopPart) {
      recordDepScope(recordNode, ns); // Record the scope for the assignment itself
    } else {
      // If not in a for loop part, handle other cases as before
      if (parent.isFunction()) { // Check if the parent is a function to handle call expressions
        traverseEdge(ns.getCaller(), ns.getType(), nameNode); // Use traverseEdge method to update referenced status
      } else {
        recordDepScope(recordNode, ns);
      }
    }
  }
}