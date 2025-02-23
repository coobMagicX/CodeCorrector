private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    int type = n.getType();
    Node parent = n.getParent();

    // Check if this variable's parent is a VAR node, indicating a variable declaration
    if (parent.isVar()) {
      // Check if the variable declaration has a child and it is a qualified name
      if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
        String name = n.getString();
        Var aliasVar = scope.getVar(name);
        if (aliasVar != null && aliasVar.getInitialValue() != null) {
          String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
          if (qualifiedName != null) {
            aliases.put(name, aliasVar);
            transformation.addAlias(name, qualifiedName);
            continue; // Skip the error reporting because it's a valid alias
          }
        }
      }
      // If the node does not qualify for the aliasing criteria above, then it's a non-alias local.
      report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
    }
  }
}
