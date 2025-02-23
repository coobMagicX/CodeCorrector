private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  if (parent == null) {
    return null;
  }

  // Handle return statements correctly within function bodies.
  if (node.isReturn()) {
    // If a return node has no explicit return value, it may need to fall through to a symbolic node or end execution.
    if (node.getFirstChild() == null) {
      // Optionally handle scenarios where no return value is provided (for void functions or ending execution).
    }
    return null; // Returning from a function ends the control flow for that function.
  }

  if (parent.isFunction()) {
    // Transferring control back to the caller of the function
    return null;
  }

  // Other existing logic for handling node types like IF, FOR, WHILE, etc..
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
          throw new IllegalStateException("Unreachable condition.");
        }
      } else {
        return computeFollowNode(fromNode, parent, cfa);
      }
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
        return NodeUtil.hasFinally(parent) ? parent.getLastChild() : computeFollowNode(fromNode, parent, cfa);
      } else if (NodeUtil.getCatchBlock(parent) == node) {
        return NodeUtil.hasFinally(parent) ? node.getNext() : computeFollowNode(fromNode, parent, cfa);
      } else if (parent.getLastChild() == node) {
        // Handling the finally block.
        return computeFollowNode(fromNode, parent, cfa);
      }
      break;
    default:
      break;
  }

  // Continue in the sibling direction unless it is a function.
  Node nextSibling = node.getNext();
  while (nextSibling != null && nextSibling.isFunction()) {
    nextSibling = nextSibling.getNext();
  }

  if (nextSibling != null) {
    return computeFallThrough(nextSibling);
  } else {
    return computeFollowNode(fromNode, parent, cfa);
  }

  // If it gets down here, return null indicating end of control flow.
  return null;
}
