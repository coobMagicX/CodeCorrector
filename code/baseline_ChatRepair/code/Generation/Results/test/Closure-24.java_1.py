private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    Node parent = n.getParent();
    if (parent.isVar()) {
      if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
        String name = n.getString();
        Var aliasVar = scope.getVar(name);
        if (aliasVar != null) { // checks if the alias refers to a variable in scope
          aliases.put(name, aliasVar);
          String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
          transformation.addAlias(name, qualifiedName);
        } else {
          report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString()); // reports if no such variable
        }
      } else {
        report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString()); // reports if child node isn't a qualified name
      }
    } else {
      report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString()); // reports if parent isn't 'var'
    }
  }

  // Verify if the current node (function declaration) meets specific conditions
  Node functionNode = t.getCurrentNode();
  if (functionNode != null && functionNode.isFunction()) {
    if (!functionNode.getParent().isScript() || functionNode.getGrandparent() != null ||
        functionNode.getChildCount() > 1 || functionNode.getFirstChild().getNext().hasChildren()) {
      t.report(functionNode, GOOG_SCOPE_INVALID_FUNCTION_ERROR); // custom error for incorrect goog.scope function usage
    }
  }
}
