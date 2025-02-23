private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    int type = n.getType();
    Node parent = n.getParent();
    if (parent.isVar()) {
      if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
        String name = n.getString();

        // Ensure there is a corresponding variable in the scope
        if (scope.isDeclared(name, true)) {
          Var aliasVar = scope.getVar(name);
          aliases.put(name, aliasVar);

          String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
          transformation.addAlias(name, qualifiedName);
        } else {
          report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, name);
        }
      } else {
        // When the node does not have qualified name child, report error
        report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
      }
    }
  }
}
