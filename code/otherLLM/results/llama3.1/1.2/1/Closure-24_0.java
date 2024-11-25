private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    int type = n.getType();
    Node parent = n.getParent();
    if (parent.isFunctionDeclaration()) { // Handle function declarations correctly
      String name = n.getString();
      Var aliasVar = scope.getVar(name);
      aliases.put(name, aliasVar);

      String qualifiedName =
          aliasVar.getInitialValue().getQualifiedName();
      transformation.addAlias(name, qualifiedName);
    } else if (parent.isVar()) {
      if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
        String name = n.getString();
        Var aliasVar = scope.getVar(name);
        aliases.put(name, aliasVar);

        String qualifiedName =
            aliasVar.getInitialValue().getQualifiedName();
        transformation.addAlias(name, qualifiedName);
      } else {
        // If local variable is private, use it as a private variable
        if (n.hasChildren() && n.getFirstChild().isIdentifier()) {
          String name = n.getString();
          Var aliasVar = scope.getVar(name);
          aliases.put(name, aliasVar);

          String qualifiedName =
              aliasVar.getInitialValue().getQualifiedName();
          transformation.addAlias(name, qualifiedName);
        } else {
          report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
        }
      }
    } 
  }
}