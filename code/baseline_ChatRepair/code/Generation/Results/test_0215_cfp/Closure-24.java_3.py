private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    Node parent = n.getParent();
    
    if (parent.isVar()) {
      // The variable is defined with var keyword
      if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
        // Variable is being assigned a value that is a qualified name
        Node firstChild = n.getFirstChild();
        String name = n.getString();
        Var aliasVar = scope.getVar(firstChild.getQualifiedName());
        
        if (aliasVar != null) {
          String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
          aliases.put(name, aliasVar);
          transformation.addAlias(name, qualifiedName);
        } else {
          // The name does not correspond to a variable in the scope, could be an error case.
          report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, name);
        }
      } else {
        // The variable is possibly not an alias as it doesn't have a qualified name assigned
        report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
      }
    } else {
      // The variable is not defined directly under a var statement, possibly an error.
      report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
    }
  }
}
