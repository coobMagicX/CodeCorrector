private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  ...
  Node parent = node.getParent();

  // A check if inserted here to handle return from function correctly.
  if (node.getType() == Token.RETURN) {
    // Checks if the function should return something but the return statement is empty
    if (!node.hasChildren()) {
      JSType returnType = node.getJSType();
      if (returnType != null && !returnType.isVoidType()) {
        return computeFollowNode(fromNode, node, cfa); // Possibly re-structure correct follow logic here
      }
    }
    // For normal return statements, transfer control back to caller of the function
    return null; 
  }

  if (parent == null || parent.isFunction() ||
      (cfa != null && node == cfa.root)) {
    return null;
  }
  ...

  // Now that we are done with the special cases follow should be its
  // immediate sibling, unless its sibling is a function
  Node nextSibling = node.getNext();

  // Skip function declarations because control doesn't get pass into it.
  while (nextSibling != null && nextSibling.isFunction()) {
    nextSibling = nextSibling.getNext();
  }

  ...
}
