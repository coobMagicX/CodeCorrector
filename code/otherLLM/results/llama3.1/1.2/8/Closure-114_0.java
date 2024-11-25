private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);
  if (ns != null) {
    if (parent.isFor()) {
      // Revisit the dependency scope calculation logic as indicated by TODO(user) comment.
      recordDepScope(nameNode, ns);
    } else {
      // The rhs of the assignment is the caller, so it's used by the context. Don't associate it w/ the lhs.
      if (parent.isFor() && !NodeUtil.isForIn(parent)) {
        // Patch for assignments that appear in the init, condition or iteration part of a FOR loop.
        recordDepScope(recordNode, ns);
      } else {
        recordDepScope(nameNode, ns);
      }
    }
  }
}