private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);

  if (ns != null) {
    // We need to find out whether the assignment is part of an executable context
    // or merely a declaration because the behavior might change the scoping.
    if (parent.isExprResult()) {
      Node grandParent = parent.getParent();
      if (grandParent != null && grandParent.isCall() && parent == grandParent.getFirstChild()) {
        // The assignment is directly used as a call (or execution) e.g., (fun = function() { x; })();
        // Here we should attach the dependency to the nameNode as it represents the functional value
        // being used or called.
        recordDepScope(nameNode, ns);
      } else {
        // Normal assignment in an expression context but not directly used as function call
        recordDepScope(recordNode, ns);
      }
    } else if (parent.isFor() && !NodeUtil.isForIn(parent)) {
      // Special handling for traditional for loops where assignment might be in init
      if (n == parent.getFirstChild()) {
        // Assignment is in the initialization of the for loop
        recordDepScope(nameNode, ns);
      } else {
        recordDepScope(recordNode, ns);
      }
    } else {
      // Default case: Assignments in all other contexts
      recordDepScope(recordNode, ns);
    }
  }
}
