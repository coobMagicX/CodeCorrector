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
        // Handle non-alias local variables
        if (!isAliasLocal(n)) {
          report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
        }
      }
    }
  }
}

private boolean isAliasLocal(Node n) {
  int type = n.getType();
  if (type == Token.FUNCTION || type == Token.FUNCTION_NAME ||
      type == Token.OBJECTLIT || type == Token.ARRAYLIT ||
      type == Token.CALL || type == Token.NEW ||
      type == Token.ASSIGN || type == Token.VAR) {
    return true;
  }
  
  if (type == Token.GETPROP || type == Token.GETELEM) {
    Node parent = n.getParent();
    if (parent != null && parent.getType() == Token.ASSIGN) {
      return true;
    }
  }
  
  return false;
}

private void report(NodeTraversal t, Node n, String errorCode, String variableName) {
  // TODO: Add implementation for reporting the error.
  // This method should handle reporting the error message based on the provided errorCode and variableName.
}