private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    Node parent = n.getParent();
    
    if (parent.isVar()) {
      if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
        String name = n.getString();
        Node initNode = n.getFirstChild();
        String qualifiedName = initNode.getQualifiedName();

        if (scope.getVar(qualifiedName) != null) {
          // This branch is for proper aliases where the node's child is a qualified name that exists in the scope.
          Var aliasVar = scope.getVar(qualifiedName);
          aliases.put(name, aliasVar);
          transformation.addAlias(name, qualifiedName);
        } else {
          // If no variable in scope matches the qualified name, this is not an alias.
          report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, name);
        }
      } else {
        // The node has no children or the first child is not a qualified name.
        report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
      }
    } else {
      // Parent of the node is not a var statement, it's likely a syntax or structural issue.
      report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
    }
  }
}
