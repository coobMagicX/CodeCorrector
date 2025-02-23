public void visit(NodeTraversal t, Node n, Node parent) {
  // Check for the nodes that can safely be ignored
  if (n.isEmpty() || n.isComma()) {
    return;
  }

  // Return immediately if no parent node
  if (parent == null) {
    return;
  }

  // Main checks for problematic node patterns with conditions based on type and structure
  if (!NodeUtil.mayHaveSideEffects(n, t.getCompiler())) {
    if (NodeUtil.isControlStructure(parent) && !NodeUtil.isConditionOfIf(n, parent) && 
        !NodeUtil.isConditionOfWhile(n, parent) && !NodeUtil.isConditionOfFor(n, parent)) {
      // Special handling for 'for' loops since it has 3 parts and each part is a potential trigger
      if (parent.isFor()) {
        if (n == parent.getFirstChild().getNext() || n == parent.getFirstChild().getNext().getNext()) {
          // Allow certain benign operations in for-loop init and step parts without side-effects
          return;
        }
      } else if (parent.isExprResult()) {
        // Report if an expression result doesn't have a side effect
        String msg = "This code lacks side-effects. Is there a bug?";
        t.getCompiler().report(t.makeError(n, level, USELESS_CODE_ERROR, msg));
      }
    }
  }

  // Further processing or traversal could be placed here, handling more specific node conditions
}
