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
      } else if (n.isFunction()) {
        report(t, n, GOOG_SCOPE_FUNCTION_DECLARATION, n.getString());
      } else {
        String name = n.getString();
        Var aliasVar = scope.getVar(name);
        if (aliasVar != null) {
          aliases.put(name, aliasVar);
        } else {
          // Report error only if variable is not an alias
          report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, name);
        }
      }
    }
  }
}