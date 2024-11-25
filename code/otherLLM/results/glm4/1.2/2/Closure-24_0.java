private void findAliases(NodeTraversal t) {
  Scope scope = t.getScope();
  for (Var v : scope.getVarIterable()) {
    Node n = v.getNode();
    int type = n.getType();
    Node parent = n.getParent();

    if (parent.isVar() && parent.getNumChildren() > 1 && parent.getFirstChild().isQualifiedName()) {
      String name = n.getString();
      Var aliasVar = scope.getVar(name);
      aliases.put(name, aliasVar);

      String qualifiedName = aliasVar.getInitialValue().getQualifiedName();
      transformation.addAlias(name, qualifiedName);
    } else if (type == Token.PRIVATE && !n.isAliased()) {
      // Check if the variable is a local private variable
      Node declaredInFunction = n.declaredInFunction();
      if (declaredInFunction != null) {
        Scope functionScope = scope.getEnclosingScope(declaredInFunction);
        Var localVar = functionScope.getVar(n.getString());
        if (localVar == null || localVar.getNode().getType() != Token.PRIVATE) {
          // This is a non-alias local private variable that needs to be reported.
          report(t, n, GOOG_SCOPE_NON_ALIAS_LOCAL, n.getString());
        }
      } else {
        // It's not declared in a function scope, so it should already be handled elsewhere.
      }
    }
  }
}