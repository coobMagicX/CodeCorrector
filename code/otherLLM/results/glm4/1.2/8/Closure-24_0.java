private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  Collection<Node> aliasDefinitionsInOrder = getAliasDefinitionsInOrder(); // Use provided method

  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    int type = n.getType();
    Node parent = n.getParent();

    if (parent.isVar() && !aliasDefinitionsInOrder.contains(n)) { // Check if not already defined as an alias
      if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
        String name = n.getString();
        Var aliasVar = scope.getVar(name);
        if (aliasVar != null) { // Ensure the variable exists in the scope
          aliases.put(name, aliasVar);

          String qualifiedName = aliasVar.getInitialValue().getQualifiedName(); // Get qualified name only if necessary
          transformation.addAlias(name, qualifiedName);
        }
      } else {
        report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
      }
    } else {
      report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString()); // Report the issue for non-alias locals
    }
  }
}