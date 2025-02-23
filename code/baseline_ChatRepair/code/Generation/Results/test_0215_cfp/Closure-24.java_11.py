private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    Node parent = n.getParent();
    
    // Ensure the parent node is a VAR declaration and the node has children
    if (parent != null && parent.isVar() && n.hasOneChild()) {
      Node rhs = n.getFirstChild();

      // Check if right-hand side is a qualified name indicating a potential alias.
      if (rhs != null && rhs.isQualifiedName()) {
        String name = n.getString(); // Get the name declared in the VAR statement.
        String qualifiedName = rhs.getQualifiedName();

        // Check if the qualified name maps to a known variable (alias check)
        if (scope.getVar(qualifiedName) != null) {
          aliases.put(name, v); // Record the alias.
          transformation.addAlias(name, qualifiedName); // Handle the alias in transformation.
        } else {
          // Error reporting for non-alias when a variable is attempted to be used as an alias but is not defined
          report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, qualifiedName);
        }
      } else {
        // Error reporting for non-alias as RHS does not qualify as an alias.
        report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
      }
    } else {
      // Additional error reporting condition if the parent is not VAR or the node does not have exact one child.
      if (parent.isVar())
        report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
    }
  }
}
