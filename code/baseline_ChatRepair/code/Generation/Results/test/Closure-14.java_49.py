private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {
  Node parent = node.getParent();

  if (parent == null || (parent.isFunction() && node.isReturn())) {
    // If node is a return statement in a function, control should go back to the caller.
    return null;  // Transfer control to the symbolic return node, indicated by a null return here.
  } else if (parent.isFunction()) {
    // If it's just inside a function but not a return, we keep checking for control flow siblings.
    return computeFollowNode(fromNode, parent, cfa);
  } else if (cfa != null && node == cfa.root) {
    // Handling the case when the node is the root in control flow analysis.
    return null;
  }

  // Existing switch and IF/WHILE/FOR handling remains unchanged as provided.
  ...

  // Handling sibling nodes and if no siblings, transferring up the AST.
  ...

}
