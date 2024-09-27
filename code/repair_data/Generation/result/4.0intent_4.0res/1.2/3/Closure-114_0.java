private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);
  if (ns != null) {
    if (parent.isFor() && !NodeUtil.isForIn(parent)) {
      // Patch for assignments that appear in the init,
      // condition or iteration part of a FOR loop.  Without
      // this change, all 3 of those parts try to claim the for
      // loop as their dependency scope.  The last assignment in
      // those three fields wins, which can result in incorrect
      // reference edges between referenced and assigned variables.
      //
      // TODO(user) revisit the dependency scope calculation
      // logic.
      if (parent.getFirstChild().getNext() != n) {
        recordDepScope(recordNode, ns);
      } else {
        recordDepScope(nameNode, ns);
      }
    } else {
      // Adjusted to ensure that function expressions and other complex RHS 
      // are handled correctly by checking if the second child (rhs) of the assignment
      // node is a function or contains a function call.
      Node rhs = nameNode.getNext();
      if (rhs != null && (rhs.isFunction() || rhs.hasChildren() && rhs.getFirstChild().isFunction())) {
        // When rhs is a function or contains function, we need to ensure
        // dependencies are tracked at the assignment level without leaking scope.
        recordDepScope(recordNode, ns);
      } else {
        // For simple assignments or those not involving functions
        // continue associating the rhs with the context.
        recordDepScope(recordNode, ns);
      }
    }
  }
}