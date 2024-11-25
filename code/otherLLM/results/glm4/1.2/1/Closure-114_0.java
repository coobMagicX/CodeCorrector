private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);
  if (ns != null) {
    // Check for assignment inside a FOR loop's init, condition or iteration
    if (parent.isFor() && !NodeUtil.isForIn(parent)) {
      // Record dependency scope correctly based on the position of the assignment
      Node prevSibling = parent.getFirstChild();
      while (prevSibling.getNext() != n) {
        prevSibling = prevSibling.getNext();
      }
      if (prevSibling == parent.getLastChild()) { // If it's the last child, record at the name node level
        recordDepScope(nameNode, ns);
      } else { // Otherwise, record at the node level
        recordDepScope(recordNode, ns);
      }
    } else {
      // For assignments that are not inside a FOR loop, handle them differently
      if (n.isAssign() && n.getPreviousSibling() != null && n.getPreviousSibling().isCall()) {
        Node callParent = n.getParent();
        // If the assignment is part of a function call expression, don't record it at the name node level
        recordDepScope(recordNode, ns);
      } else {
        // For other cases, record as usual
        if (n.isAssign()) {
          recordDepScope(nameNode, ns);
        } else {
          recordDepScope(recordNode, ns);
        }
      }
    }
  }
}