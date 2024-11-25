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
        aliases.put(name, aliasVar);

        String qualifiedName =
            aliasVar.getInitialValue().getQualifiedName();
        transformation.addAlias(name, qualifiedName);
        // Bleeding functions already get a BAD_PARAMETERS error, so just
        // do nothing.
        // Parameters of the scope function also get a BAD_PARAMETERS
        // error.
      } else {
        if (!shouldTraverse(t, n, parent)) { // Check if we should traverse into this scope
          report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
        }
      }
    }
  }
}