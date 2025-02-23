private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    Node parent = n.getParent();
    
    if (parent.isVar()) {
      if (n.hasChildren() && n.getFirstChild().isName()) {
        // Expected: Alias declarations must be direct variable names (isName checks this)
        String name = n.getString();
        Node initNode = n.getFirstChild();
        String referencedName = initNode.getString();

        // Check that a variable is indeed being referenced directly.
        Var aliasVar = scope.getVar(referencedName);
        if (aliasVar != null) {
          String qualifiedName = aliasVar.getName();
          aliases.put(name, aliasVar);
          transformation.addAlias(name, qualifiedName);
        } else {
          // Referred variable is not found in the current scope - not an alias.
          report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, name);
        }
      } else {
        // It's not a direct reference, so it should be reported as not an alias.
        report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
      }
    } else {
      // Handle other misplacements or structural errors potentially.
      report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
    }
  }
}
