private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);
  if (ns != null) {
    if (parent.isFor()) {
      // Check if the assignment is in the init part of a FOR loop
      if (NodeUtil.isInit(parent)) {
        recordDepScope(nameNode, ns);
      } 
      // Check if the assignment is in the condition or iteration part of a FOR loop
      else if (NodeUtil.isConditionOrIteration(parent)) {
        Node ancestor = n.getAncestor(Node.FOR);
        if (ancestor != null) {
          recordDepScope(recordNode, ns);
        } else {
          recordDepScope(nameNode, ns);
        }
      } 
      // If the assignment is not in any part of a FOR loop, proceed as before
      else {
        if (!NodeUtil.isForIn(parent)) {
          if (parent.getFirstChild().getNext() != n) {
            recordDepScope(recordNode, ns);
          } else {
            recordDepScope(nameNode, ns);
          }
        } else {
          recordDepScope(recordNode, ns);
        }
      }
    } else {
      // The rhs of the assignment is the caller, so it's used by the context.
      // Don't associate it w/ the lhs.
      recordDepScope(recordNode, ns);
    }
  }
}