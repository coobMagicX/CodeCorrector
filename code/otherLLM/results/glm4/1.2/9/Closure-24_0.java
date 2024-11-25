private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    int type = n.getType();
    Node parent = n.getParent();
    if (parent.isFunction() || parent.isMethod()) {
      // Check if the node is a local variable within a function or method.
      boolean isLocalVariable = type == Token.VAR && parent.isFunction() || parent.isMethod();
      if (isLocalVariable) {
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
          // Local variables that are not aliases should be reported as such.
          report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
        }
      }
    } else {
      // If it's not within a function or method, report the issue accordingly.
      report(t, n, GOOG_SCOPE_OUTSIDE_FUNCTION, n.getString());
    }
  }
}