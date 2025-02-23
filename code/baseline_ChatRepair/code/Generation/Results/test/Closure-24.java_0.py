private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    int type = n.getType();
    Node parent = n.getParent();
    if (parent.isVar()) {
      if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
        String name = n.getString();
        Var aliasVar = scope.getVar(name);
        if (aliasVar != null) {
          aliases.put(name, aliasVar);
          String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
          transformation.addAlias(name, qualifiedName);
        } else {
          // If aliasVar is null, this is not a valid alias and should report an error
          report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
        }
      } else {
        // Child node is not a qualified name which can't form a valid alias
        report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
      }
    } else {
      // Parent is not 'var' which means the variable cannot be an alias in goog.scope
      // Properly report this as an error case
      report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
    }
  }
}
