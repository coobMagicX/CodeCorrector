private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node valueNode = n.getLastChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);

  if (ns != null) {
    if (parent.isFor() && !NodeUtil.isForIn(parent)) {
      if (parent.getFirstChild().getNext() != n) {
        recordDepScope(recordNode, ns);
      } else {
        recordDepScope(nameNode, ns);
      }
    } else if (valueNode.isFunction() && isFunctionImmediatelyInvoked(n, valueNode)) {
      // Special handling for immediate function assignments like (fun = function() {})();
      recordDepScope(nameNode, ns);
    } else {
      // Default case: The rhs of the assignment influences its context
      recordDepScope(recordNode, ns);
    }
  }
}

/**
 * Check if a function assigned to a variable is immediately invoked.
 * Typical Pattern: (varFun = function(){ ... })();
 */
private boolean isFunctionImmediatelyInvoked(Node assignmentNode, Node functionNode) {
  // Check if parent node is an immediate call
  Node parent = assignmentNode.getParent();
  if (parent.isCall() && parent.getFirstChild() == assignmentNode) {
      return true;
  }
  return false;
}
