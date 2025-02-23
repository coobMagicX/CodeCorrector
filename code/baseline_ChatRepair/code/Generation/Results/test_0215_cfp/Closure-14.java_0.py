private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();
  if (parent == null || (parent.isFunction() && node != parent.getLastChild()) ||
      (cfa != null && node == cfa.root)) {
    return null;
  }

  // Handle return statements and the 'end' of functions more explicitly
  if (node.isReturn()) {
    // A return statement should transfer control to the caller.
    return parent.isFunction() ? null : computeFollowNode(fromNode, parent, cfa);
  }

  // Reach the end of function without return should lead to the auto-inserted return if function declares a return type.
  if (parent.isFunction() && node == parent.getLastChild()) {
    // Assuming the function type can be confirmed if it needs a return, add such a hypothetical method:
    // This will check if the function should return a value and handle accordingly.
    if (node.isBlock() && shouldBeReturningValue(parent)) {
      // We should ensure this block ends with a return or is reaching an automatic return
      Node returnTypeNode = new Node(Token.RETURN);
      returnTypeNode.setLineno(node.getLineno());
      return returnTypeNode; // Hypothetical generated return node
    } else {
      return null;
    }
  }

  // Continue with the rest of the code for other cases

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

private static boolean shouldBeReturningValue(Node functionNode) {
  // Pseudocode: Check if the function declares a non-void return type, perhaps via JSType.
  JSType returnType = functionNode.getJSType().toMaybeFunctionType().getReturnType();
  return returnType != null && !returnType.isVoidType();
}
