private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
  if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
    FunctionType fnType = type.toMaybeFunctionType();
    // Check if fnType is null and handle edge cases
    if (fnType == null) { 
      report(t, n, CONSTRUCTOR_TYPE_ERROR);
      ensureTyped(t, n);
    } else {
      visitParameterList(t, n, fnType);
      ensureTyped(t, n, fnType.getInstanceType());
    }
  } else {
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n);
  }
}