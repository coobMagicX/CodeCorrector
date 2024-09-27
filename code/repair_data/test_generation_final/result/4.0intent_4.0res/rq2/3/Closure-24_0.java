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
          // Handle case where aliasVar is null, implying no alias was found
          report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
        }
      } else if (type == Token.FUNCTION) {
        // Function declarations are not considered aliases
        // Skip processing for function declarations
        continue;
      } else {
        // For non-qualified names that are not function declarations
        report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
      }
    }
  }
}