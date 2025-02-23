private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  if (parent == null || parent.isFunction() || node.getType() == Token.RETURN ||
      (cfa != null && node == cfa.root)) {
    if (node.getType() == Token.RETURN && parent != null && parent.isFunction() && !node.hasChildren()) {
      // Handle case when function is expected to return a value, but 'return' has no expression
      JSType functionReturnType = parent.getJSType().toMaybeFunctionType().getReturnType();
      if (functionReturnType != null && !functionReturnType.isVoidType()) {
        throw new IllegalStateException("Missing return value in function expected to return " + functionReturnType);
      }
    }
    return null;
  }

  switch (parent.getType()) {
    case Token.IF:
      return computeFollowNode(fromNode, parent, cfa);
    case Token.CASE:
    case Token.DEFAULT_CASE:
      if (parent.getNext() != null) {
        return computeFollowNode(fromNode, parent.getNext().getFirstChild(), cfa);
      } else {
        return computeFollowNode(fromNode, parent, cfa);
      }
    case Token.FOR:
      if (NodeUtil.isForIn(parent)) {
        return parent.getFirstChild();
      } else {
        return parent.getFirstChild().getNext().getNext();
      }
    case Token.WHILE:
    case Token.DO:
      return parent;
    case Token.TRY:
      if (parent.getFirstChild() == node) {
        if (NodeUtil.hasFinally(parent)) {
          return parent.getLastChild();
        } else {
          return computeFollowNode(fromNode, parent, cfa);
        }
      } else if (NodeUtil.getCatchBlock(parent) == node) {
        if (NodeUtil.hasFinally(parent)) {
          return parent.getLastChild();
        } else {
          return computeFollowNode(fromNode, parent, cfa);
        }
      } else if (parent.getLastChild() == node) {
        return computeFollowNode(fromNode, parent, cfa);
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
