private static Node computeFollowNode(
    Node fromNode, Node node, ControlFlowAnalysis cfa) {

  Node parent = node.getParent();
  
  if (parent == null || node.isReturn()) {
    // If the parent is null or the node is a return statement, we need to handle control transfer.
    if (cfa != null) {
      ControlFlowAnalysis.SymbolicReturn symbolicReturn = cfa.getSymbolicReturn(node);
      if (symbolicReturn != null) {
        return symbolicReturn;
      }
    }
    return null; // If no symbolic return is defined, return null.
  }

  if (parent.isFunction() || (cfa != null && node == cfa.root)) {
    // Handling the transfer of control back to the caller of the function or when the node is the root node in CFA.
    return null;
  }

  // Rest of the method continues as originally outlined.
  // Including cases for IF, FOR, WHILE, DO, TRY etc.

  // Remainder of code unchanged...
}
