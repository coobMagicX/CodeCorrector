private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  
  // Add handling for RETURN node specifically
  if (node.isReturn()) {
    // Runtime behavior should consider the flow context (if cfa provides specific handling for returns)
    return parent.isFunction() ? null : computeFollowNode(fromNode, parent, cfa);
  }

  if (parent == null || parent.isFunction() || (cfa != null && node == cfa.root)) {
    return null;
  }

  // Existing switch and other logic remains the same
  switch (parent.getType()) {
    case Token.IF:
      return computeFollowNode(fromNode, parent, cfa);
 
    // Cases for SWITCH handling
    case Token.CASE:
    case Token.DEFAULT_CASE:
      if (parent.getNext() != null) {
        Node nextCase = parent.getNext();
        while (nextCase != null && (nextCase.isCase() || nextCase.isDefaultCase())) {
          Node caseBody = nextCase.getFirstChild().getNext();
          if (caseBody != null && !caseBody.isEmpty()) {
            return caseBody;
          }
          nextCase = nextCase.getNext();
        }
      }
      return computeFollowNode(fromNode, parent, cfa);

    // Other loop and control structures handling remains identical
    // Insert the rest of unmodified switch cases and final sibling retrieval logic here
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
