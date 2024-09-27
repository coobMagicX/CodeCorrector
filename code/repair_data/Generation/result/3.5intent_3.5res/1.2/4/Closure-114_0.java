private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);
  if (ns != null) {
    if ((parent.isFor() && !NodeUtil.isForIn(parent)) || parent.isCall()) {
      // Patch for assignments that appear in the init,
      // condition or iteration part of a FOR loop or in a function call.
      // Without this change, all 3 of those parts try to claim the for
      // loop or the function call as their dependency scope. The last assignment in
      // those three fields wins, which can result in incorrect
      // reference edges between referenced and assigned variables.
      //
      // TODO(user) revisit the dependency scope calculation
      // logic.
      if (parent.getFirstChild().getNext() != n) {
        recordDepScope(recordNode, ns);
      } else {
        if (recordNode.isCall()) {
          recordDepScope(recordNode.getFirstChild(), ns);
        } else {
          recordDepScope(nameNode, ns);
        }
      }
    } else {
      // The rhs of the assignment is the caller, so it's used by the
      // context. Don't associate it w/ the lhs.
      // FYI: this fixes only the specific case where the assignment is the
      // caller expression, but it could be nested deeper in the caller and
      // we would still get a bug.
      // See testAssignWithCall2 for an example of this.
      if (recordNode.isCall()) {
        recordDepScope(recordNode.getFirstChild(), ns);
      } else {
        recordDepScope(recordNode, ns);
      }
    }
  }
}