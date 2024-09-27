private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);
  if (ns != null) {
    if (parent.isFor() && !NodeUtil.isForIn(parent)) {
      if (parent.getFirstChild().getNext() != n) {
        // Check if the rhs of the assignment is a function expression
        if (n.getLastChild().isFunction()) {
          // Assign the dependency scope to the function expression
          recordDepScope(n.getLastChild(), ns);
        } else if (NodeUtil.isFunctionExpression(n.getLastChild())) {
          // Handle nested function expression assignments
          recordDepScope(n.getLastChild(), ns);
        } else {
          // Assign the dependency scope to the record node
          recordDepScope(recordNode, ns);
        }
      } else {
        recordDepScope(nameNode, ns);
      }
    } else {
      // Check if the rhs of the assignment is a function expression
      if (n.getLastChild().isFunction()) {
        // Assign the dependency scope to the function expression
        recordDepScope(n.getLastChild(), ns);
      } else if (NodeUtil.isFunctionExpression(n.getLastChild())) {
        // Handle nested function expression assignments
        recordDepScope(n.getLastChild(), ns);
      } else {
        // Assign the dependency scope to the record node
        recordDepScope(recordNode, ns);
      }
    }
  }
}