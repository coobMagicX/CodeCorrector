private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
  // This is the case where:
  //
  // 1. Parent is null implies that we are transferring control to the end of
  // the script.
  //
  // 2. Parent is a function implies that we are transferring control back to
  // the caller of the function.
  //
  // 3. If the node is a return statement, we should also transfer control
  // back to the caller of the function.
  //
  // 4. If the node is root then we have reached the end of what we have been
  // asked to traverse.
  //
  // In all cases we should transfer control to a "symbolic return" node.
  // This will make life easier for DFAs.
  Node parent = node.getParent();
  if (parent == null || parent.isFunction() ||
      (cfa != null && node == cfa.root)) {
    return createSymbolicReturnNode(); // Use a method to create a symbolic return node
  }

  // If we are just before a IF/WHILE/DO/FOR:
  switch (parent.getType()) {
    // The follow() of any of the path from IF would be what follows IF.
    case Token.IF:
      return computeFollowNode(fromNode, parent, cfa);
    case Token.CASE:
    case Token.DEFAULT_CASE:
      // After the body of a CASE, the control goes to the body of the next
      // case, without having to go to the case condition.
      if (parent.getNext() != null) {
        Node nextCase = parent.getNext().isCase() ? parent.getNext().getFirstChild().getNext()
                                                  : (parent.getNext().isDefaultCase() ? parent.getNext().getFirstChild() : null);
        return nextCase != null ? computeFallThrough(nextCase) : computeFollowNode(fromNode, parent, cfa);
      } else {
        return computeFollowNode(fromNode, parent, cfa);
      }
      break;
    case Token.FOR:
      if (NodeUtil.isForIn(parent)) {
        return parent;
      } else {
        return computeFallThrough(parent.getFirstChild().getNext().getNext());
      }
    case Token.WHILE:
    case Token.DO:
      return computeFallThrough(node.getNext());
    case Token.TRY:
      // If we are coming out of the TRY block...
      if (parent.getFirstChild() == node) {
        if (NodeUtil.hasFinally(parent)) { // and have FINALLY block.
          return computeFallThrough(parent.getLastChild());
        } else { // and have no FINALLY.
          return computeFollowNode(fromNode, parent, cfa);
        }
      // CATCH block.
      } else if (NodeUtil.getCatchBlock(parent) == node){
        if (NodeUtil.hasFinally(parent)) { // and have FINALLY block.
          return computeFallThrough(node.getNext());
        } else {
          return computeFollowNode(fromNode, parent, cfa);
        }
      // If we are coming out of the FINALLY block...
      } else if (parent.getLastChild() == node){
        if (cfa != null) {
          for (Node finallyNode : cfa.finallyMap.get(parent)) {
            cfa.createEdge(fromNode, Branch.UNCOND, finallyNode);
          }
        }
        return computeFollowNode(fromNode, parent, cfa);
      }
  }

  // Now that we are done with the special cases follow should be its
  // immediate sibling, unless its sibling is a function
  Node nextSibling = node.getNext();

  // Skip function declarations as they do not affect control flow.
  while (nextSibling != null && nextSibling.getType() == Token.FUNCTION_DECLARATION) {
    nextSibling = nextSibling.getNext();
  }

  if (nextSibling != null) {
    return computeFallThrough(nextSibling);
  } else {
    return computeFollowNode(fromNode, parent, cfa); // If no sibling, move up the AST
  }
}

private static Node createSymbolicReturnNode() {
  // Implementation to create a symbolic return node
  return new Node(Token.SYMBOLIC_RETURN);
}

// Assuming computeFallThrough is already defined in the context.