private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();

  if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
    FunctionType fnType = type.toMaybeFunctionType();
    if (fnType != null) {
      visitParameterList(t, n, fnType);

      // Check if the instance type of the function is not a constructor to ensure proper handling
      if (!fnType.getInstanceType().isConstructor()) {
        ensureTyped(t, n, fnType.getInstanceType());
      } else {
        report(t, n, NOT_A_CONSTRUCTOR);
      }
    } else {
      // If there's no FunctionType, it should not be a constructor
      report(t, n, NOT_A_CONSTRUCTOR);
      ensureTyped(t, n);
    }
  } else {
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n);
  }
}