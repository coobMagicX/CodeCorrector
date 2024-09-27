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

        // Check if the variable is a function declaration
        if (aliasVar.getNode().isFunction()) {
          // Handle function declarations separately
          continue;
        }

        // Check if the variable has an initial value
        if (aliasVar.getInitialValue() != null) {
          aliases.put(name, aliasVar);

          String qualifiedName =
              aliasVar.getInitialValue().getQualifiedName();
          transformation.addAlias(name, qualifiedName);
        } else {
          // Variable has no initial value, report error
          report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
        }
      } else {
        // TODO(robbyw): Support using locals for private variables.
        report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
      }
    }
  }
}