@Override public void visit(NodeTraversal t, Node n, Node parent) {
  if (t.inGlobalScope()) {
    return;
  }

  if (n.isReturn() && n.getFirstChild() != null) {
    data.get(t.getScopeRoot()).recordNonEmptyReturn();
  }

  if (t.getScopeDepth() <= 2) {
    // The first-order function analyzer looks at two types of variables:
    //
    // 1) Local variables that are assigned in inner scopes ("escaped vars")
    //
    // 2) Local variables that are assigned more than once.
    //
    // We treat all global variables as escaped by default, so there's
    // no reason to do this extra computation for them.
    return;
  }

  if (n.isName() && NodeUtil.isLValue(n) &&
      // Be careful of bleeding functions, which create variables
      // in the inner scope, not the scope where the name appears.
      !NodeUtil.isBleedingFunctionName(n)) {
    String name = n.getString();
    Scope scope = t.getScope();
    Var var = scope.getVar(name);
    if (var != null) {
      Scope ownerScope = var.getScope();
      // Ensure that the type of the variable matches the expected type
      // when the variable is in a local scope that is different from the ownerScope.
      if (scope != ownerScope && ownerScope.isLocal()) {
        Type expectedType = ownerScope.getExpectedType(name);
        Type actualType = var.getType();
        if (expectedType != null && !expectedType.equals(actualType)) {
          throw new TypeMismatchException("Type mismatch for variable '" + name + "': expected " + expectedType + ", found " + actualType);
        }
        data.get(ownerScope.getRootNode()).recordEscapedVarName(name);
      }
    }
  } else if (n.isGetProp() && n.isUnscopedQualifiedName() &&
      NodeUtil.isLValue(n)) {
    String name = NodeUtil.getRootOfQualifiedName(n).getString();
    Scope scope = t.getScope();
    Var var = scope.getVar(name);
    if (var != null) {
      Scope ownerScope = var.getScope();
      if (scope != ownerScope && ownerScope.isLocal()) {
        // Ensure that the type of the qualified name matches the expected type
        // when the variable is in a local scope that is different from the ownerScope.
        Type expectedType = ownerScope.getExpectedType(n.getQualifiedName());
        Type actualType = var.getType();
        if (expectedType != null && !expectedType.equals(actualType)) {
          throw new TypeMismatchException("Type mismatch for property '" + n.getQualifiedName() + "': expected " + expectedType + ", found " + actualType);
        }
        data.get(ownerScope.getRootNode())
            .recordEscapedQualifiedName(n.getQualifiedName());
      }
    }
  }
}