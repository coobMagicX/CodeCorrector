private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    int type = n.getType();
    Node parent = n.getParent();
    if (parent.isVar() && !isSpecialCase(parent)) {
      // Check for qualified name from children or directly
      if (n.hasChildren() && n.getFirstChild().isQualifiedName()) {
        String name = n.getString();
        Var aliasVar = scope.getVar(name);
        aliases.put(name, aliasVar);

        String qualifiedName =
            aliasVar.getInitialValue().getQualifiedName();
        transformation.addAlias(name, qualifiedName);
      }
    } else if (!n.isDeclaration() && !isSpecialCase(n)) {
      // Do not report on declarations or special cases
      report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
    }
  }
}

// Helper method to determine if a node is in a special case scenario
private boolean isSpecialCase(Node node) {
  // Add any necessary conditions here for what constitutes a 'special case'
  // For example:
  if (node.isDeclaration() || node.matches(new NodeMatcher() {
      @Override
      public boolean matches(Node n) {
        return n.isFunction() && "goog.dom".equals(n.getQualifiedName());
      }
    })) {
      return true;
    }
  return false;
}