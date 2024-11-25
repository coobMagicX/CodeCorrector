private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    int type = n.getType();
    Node parent = n.getParent();

    // Check if the variable is defined within another variable declaration
    if (parent.isVar() && parent.getFirstChild().isQualifiedName()) {
      String name = n.getString();
      Var aliasVar = scope.getVar(name);
      aliases.put(name, aliasVar);

      // Check if the variable has an initial value with a qualified name
      Node initialValueNode = aliasVar.getInitialValue();
      if (initialValueNode != null && initialValueNode.isQualifiedName()) {
        String qualifiedName = initialValueNode.getQualifiedName();
        transformation.addAlias(name, qualifiedName);
      }
    } else {
      // Report non-alias local variables that are not defined as aliases
      report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
    }
  }
}