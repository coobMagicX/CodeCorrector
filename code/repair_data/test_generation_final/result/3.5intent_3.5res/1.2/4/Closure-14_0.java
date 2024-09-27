private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
  // Rest of the code...
  
  case Token.TRY:
    // If we are coming out of the TRY block...
    if (parent.getFirstChild() == node) {
      if (NodeUtil.hasFinally(parent)) { // and have FINALLY block.
        return computeFallThrough(parent.getLastChild());
      } else { // and have no FINALLY.
        // Check if a return statement is encountered within the try block
        Node returnNode = NodeUtil.getEnclosingReturn(node);
        if (returnNode != null) {
          // If there is a return statement, return the follow node of the return statement
          return computeFollowNode(fromNode, returnNode, cfa);
        } else {
          return computeFollowNode(fromNode, parent, cfa);
        }
      }
    // Rest of the code...
    }
}

private static Node getEnclosingReturn(Node node) {
  Node parent = node.getParent();
  while (parent != null) {
    if (parent.isFunction()) {
      return null; // Reached the top-level function, no return statement found
    }
    if (parent.isReturn()) {
      return parent; // Found the enclosing return statement
    }
    parent = parent.getParent();
  }
  return null; // No enclosing return statement found
}