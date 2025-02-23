private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    int type = n.getType();
    Node parent = n.getParent();
    
    // Ensure node is part of a Variable declaration and has children that can be considered for aliasing
    if (parent.isVar() && n.hasChildren()) {
      Node child = n.getFirstChild();
      
      // Check if the first child is a qualified name which indicates potential aliasing
      if (child.isQualifiedName()) {
        String name = child.getQualifiedName();
        
        // Check if the variable is already declared and retrieve it
        Var aliasVar = scope.getVar(name);
        if (aliasVar != null && aliasVar.getInitialValue() != null) {
          aliases.put(name, aliasVar);
          String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
          transformation.addAlias(name, qualifiedName);
        }
      } else {
        // Report GOOG_SCOPE_NON_ALIAS_LOCAL if not qualified for aliasing
        report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
      }
    } else {
      // If the parent is not a VAR node or node does not have children suitable for aliasing
      report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
    }
  }
}
