private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    int type = n.getType();
    Node parent = n.getParent();

    // Check if the variable is not in a function or class scope
    if (!parent.isFunction() && !parent.isClass()) {
      continue;
    }

    if (parent.isVar()) {
      if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
        String name = n.getString();
        Var aliasVar = scope.getVar(name);
        aliases.put(name, aliasVar);

        String qualifiedName =
            aliasVar.getInitialValue().getQualifiedName();
        transformation.addAlias(name, qualifiedName);
      } else if (parent.isParameter()) {
        // Parameters of the function do not count as aliases
        continue;
      }
    } else {
      // TODO(robbyw): Support using locals for private variables.
      report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
    }
  }
}