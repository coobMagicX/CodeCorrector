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
      } else if (!n.isLocal()) { // Added condition to check for non-local variables
        report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
      }
    } else {
      report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString()); // Updated the report statement to handle non-alias locals
    }
  }
}