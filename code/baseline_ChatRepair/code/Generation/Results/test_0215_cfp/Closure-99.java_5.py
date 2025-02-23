public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
  if (n.getType() == Token.FUNCTION) {
    JSDocInfo jsDoc = getFunctionJsDocInfo(n);
    if (jsDoc != null &&
        (jsDoc.isConstructor() ||
         jsDoc.hasThisType() ||
         jsDoc.isOverride())) {
      return false;
    }

    // Restrict function traversal based on parent node type
    int pType = parent.getType();
    if (!(pType == Token.BLOCK ||
          pType == Token.SCRIPT ||
          pType == Token.NAME ||
          pType == Token.ASSIGN)) {
      return false;
    }
  }

  // Handling prototype assignment conditions more robustly
  if (parent != null && parent.getType() == Token.ASSIGN) {
    Node lhs = parent.getFirstChild();

    if (n != lhs && nodeReferencesPrototype(lhs)) {
        return false;
    }
  }

  return true;
}

/**
 * Checks recursively if any part of the node is referencing a 'prototype' or misspelled 'protoype'.
 * This function navigates downward through potential property chains.
 */
private boolean nodeReferencesPrototype(Node node) {
  if (node == null) {
    return false;
  }
  String checkStr = node.getQualifiedName();
  if (checkStr != null && (checkStr.contains(".prototype.") || checkStr.contains(".protoype."))) {
    return true;
  }
  if (node.getType() == Token.GETPROP && ("prototype".equals(node.getString()) || "protoype".equals(node.getString()))) {
    return true;
  }
  // Recursively check for deeper property chain that might include a prototype reference
  return nodeReferencesPrototype(node.getFirstChild()) || nodeReferencesPrototype(node.getSecondChild());
}
