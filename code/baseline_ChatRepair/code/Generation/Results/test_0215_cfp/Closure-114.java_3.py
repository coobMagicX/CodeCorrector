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
      // Check if the parent or any of its ancestor node up to the function definition 
      // is a call node or a parameter of a call.
      boolean isInFunctionCallParameter = false;
      for (Node ancestor = parent; ancestor != null && !ancestor.isFunction(); ancestor = ancestor.getParent()) {
        if (ancestor.isCall()) {
          isInFunctionCallParameter = true;
          break;
        }
      }

      if (isInFunctionCallParameter) {
        // If the assignment is within a function call's parameters, associate the scope differently
        recordDepScope(nameNode, ns);
      } else {
        recordDepScope(recordNode, ns);
      }
    }
  }
}
