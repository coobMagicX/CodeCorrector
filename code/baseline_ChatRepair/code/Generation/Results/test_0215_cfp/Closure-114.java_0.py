private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);

  if (ns != null) {
    if (parent.isFor() && !NodeUtil.isForIn(parent)) {
      // Correctly handle assignments inside a FOR loop
      if (parent.getFirstChild().getNext() == nameNode) {
        recordDepScope(nameNode, ns);
      } else {
        recordDepScope(recordNode, ns);
      }
    } else {
      // General case: we need to consider if the assignment itself can be the part of an expression
      Node grandparent = parent.getParent();
      if (grandparent != null && (grandparent.isCall() || grandparent.isNew()) && grandparent.getFirstChild() == parent) {
        // The assignment is part of an expression used in a call or 'new' expression
        recordDepScope(nameNode, ns); // recording the variable name that is assigned, not the entire assignment
      } else {
        // Normal case: the rhs of the assignment shouldn't determine the dependency scope of the lhs
        recordDepScope(recordNode, ns);
      }
    }
  }
}
