private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    int type = n.getType();
    Node parent = n.getParent();

    // Check if the node is a local variable and not an alias
    if (!isAlias(n, scope)) {
      report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
    } else {
      // If it's an alias, continue to process
      processAlias(n, parent, scope);
    }
  }
}

private boolean isAlias(Node n, Scope scope) {
  if (parent.isVar()) { // Check if the parent node is a variable
    Node firstChild = n.getFirstChild();
    if (n.hasChildren() && firstChild.isQualifiedName()) { // Check for qualified name
      String name = n.getString();
      Var aliasVar = scope.getVar(name);
      return aliasVar != null; // Return true if an alias var exists
    }
  }
  return false; // Not an alias, hence false
}

private void processAlias(Node n, Node parent, Scope scope) {
  String name = n.getString(); // Get the variable name
  Var aliasVar = scope.getVar(name);
  aliases.put(name, aliasVar);

  String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
  transformation.addAlias(name, qualifiedName);
}