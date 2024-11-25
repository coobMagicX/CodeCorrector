private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    int type = n.getType();
    Node parent = n.getParent();
    if (!parent.isVar() || !n.hasChildren() || !n.getFirstChild().isQualifiedName()) {
      // Refine the isVar() check and handle local variables correctly
      if (parent.isVar() && n.hasChildren() && n.getFirstChild().isQualifiedName()) {
        String name = n.getString();
        Var aliasVar = scope.getVar(name);
        aliases.put(name, aliasVar);

        String qualifiedName =
            aliasVar.getInitialValue().getQualifiedName();
        transformation.addAlias(name, qualifiedName);
      } else if (parent.isVar() && !n.hasChildren()) {
        // Correctly handle local variables
        report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
      }
    } else {
      String name = n.getString();
      Var aliasVar = scope.getVar(name);
      aliases.put(name, aliasVar);

      String qualifiedName =
          aliasVar.getInitialValue().getQualifiedName();
      transformation.addAlias(name, qualifiedName);
    }
  }
}