private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  /*
   * This is the case where:
   *
   * 1. Parent is null implies that we are transferring control to the end of
   * the script.
   *
   * 2. Parent is a function implies that we are transferring control back to
   * the caller of the function.
   *
   * 3. If the node is a return statement, we should also transfer control
   * back to the caller of the function.
   *
   * 4. If the node is root then we have reached the end of what we have been
   * asked to traverse.
   *
   * In all cases we should transfer control to a "symbolic return" node.
   * This will make life easier for DFAs.
   */
  Node parent = node.getParent();
  if (parent == null || parent.isFunction() ||
      (cfa != null && node == cfa.root)) {
    return null;
  }

  // If we are just before a IF/WHILE/DO/FOR:
  switch (parent.getType()) {
    case Token.IF:
      return computeFollowNode(fromNode, parent, cfa);
    case Token.CASE:
    case Token.DEFAULT_CASE:
      if (parent.getNext() != null) {
        if (parent.getNext().isCase()) {
          return parent.getNext().getFirstChild().getNext();
        } else if (parent.getNext().isDefaultCase()) {
          return parent.getNext().getFirstChild();
        } else {
          Preconditions.checkState(false, "Not reachable");
        }
      } else {
        return computeFollowNode(fromNode, parent, cfa);
      }
      break;
    case Token.FOR:
      if (NodeUtil.isForIn(parent)) {
        return parent;
      } else {
        return parent.getFirstChild().getNext().getNext();
      }
    case Token.WHILE:
    case Token.DO:
      return parent;
    case Token.TRY:
      if (parent.getFirstChild() == node) {
        if (NodeUtil.hasFinally(parent)) {
          // Compute follow node for the first child of the finally block
          return computeFollowNode(fromNode, parent.getLastChild().getFirstChild(), cfa);
        } else {
          return computeFollowNode(fromNode, parent, cfa);
        }
      } else if (NodeUtil.getCatchBlock(parent) == node){
        if (NodeUtil.hasFinally(parent)) {
          // Compute follow node for the first child of the finally block
          return computeFollowNode(fromNode, parent.getLastChild().getFirstChild(), cfa);
        } else {
          return computeFollowNode(fromNode, parent, cfa);
        }
      } else if (parent.getLastChild() == node) {
        // Ensure that after executing finally block, control is transferred correctly
        Node followNode = cfa.getFollowingNode(parent);
        return followNode != null ? followNode : computeFollowNode(fromNode, parent, cfa);
      }
      break;
  }

  Node nextSibling = node.getNext();

  while (nextSibling != null && nextSibling.isFunction()) {
    nextSibling = nextSibling.getNext();
  }

  if (nextSibling != null) {
    return computeFallThrough(nextSibling);
  } else {
    return computeFollowNode(fromNode, parent, cfa);
  }
}