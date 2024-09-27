private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);
  if (ns != null) {
    if (parent.isFor() && !NodeUtil.isForIn(parent)) {
      if (parent.getFirstChild().getNext() != n) {
        recordDepScope(recordNode, ns);
      } else {
        recordDepScope(nameNode, ns);
      }
    } else {
      // The rhs of the assignment is the caller, so it's used by the
      // context. Don't associate it w/ the lhs.
      // FYI: this fixes only the specific case where the assignment is the
      // caller expression, but it could be nested deeper in the caller and
      // we would still get a bug.
      // See testAssignWithCall2 for an example of this.
      if (recordNode.isAssign()) {
        Node rhs = recordNode.getLastChild();
        if (rhs.isCall()) {
          Node callee = rhs.getFirstChild();
          if (callee.isName()) {
            String calleeName = callee.getString();
            NameInformation calleeInfo = createNameInformation(t, callee);
            recordDepScope(callee, calleeInfo);
          }
        }
      } else if (recordNode.isCall()) {
        Node callee = recordNode.getFirstChild();
        if (callee.isName()) {
          String calleeName = callee.getString();
          NameInformation calleeInfo = createNameInformation(t, callee);
          recordDepScope(callee, calleeInfo);
        }
      }
      recordDepScope(recordNode, ns);
    }
  }
}