private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    int type = n.getType();
    Node parent = n.getParent();
    
    if (parent.isVar()) { // We only care about variables declared with var
      if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
        String name = n.getString();
        Var aliasVar = scope.getVar(name);
        if (aliasVar != null) {
          aliases.put(name, aliasVar);
          if (aliasVar.getInitialValue() != null && aliasVar.getInitialValue().isQualifiedName()) {
            String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
            transformation.addAlias(name, qualifiedName);
          } else {
            report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, name);
          }
        } else {
          report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, name);
        }
      } else {
        // This block handles the case where n is not a qualified name or has no children.
        report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
      }
    }
  }
}
