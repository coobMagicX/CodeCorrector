private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
  Node nameNode = n.getFirstChild();
  Node parent = n.getParent();
  NameInformation ns = createNameInformation(t, nameNode);
  if (ns != null) {
    // Check if the parent is a for loop and not a for-in loop
    if (parent.isFor() && !NodeUtil.isForIn(parent)) {
      // Determine if the current node 'n' is the init, condition or iteration part of the for loop
      boolean isInitOrCondition = false;
      Node firstChild = parent.getFirstChild();
      int childIndex = 0;

      while (firstChild != null) {
        if (childIndex == 0 && n.equals(firstChild)) { // Init part
          isInitOrCondition = true;
          break;
        } else if (childIndex == 1 && n.equals(firstChild)) { // Condition part
          isInitOrCondition = true;
          break;
        } else if (childIndex == 2 && n.equals(firstChild)) { // Iteration part
          isInitOrCondition = true;
          break;
        }
        firstChild = firstChild.getNext();
        childIndex++;
      }

      if (isInitOrCondition) {
        // Record the dependency scope for the appropriate node (init, condition, or iteration)
        recordDepScope(recordNode, ns);
      } else {
        // If not init, condition, or iteration part of a for loop, use the name node
        recordDepScope(nameNode, ns);
      }
    } else {
      // The rhs of the assignment is the caller, so it's used by the context. Don't associate it with the lhs.
      recordDepScope(recordNode, ns);
    }
  }
}