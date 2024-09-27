private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
  if (type.isConstructor()) {
    FunctionType fnType = type.toMaybeFunctionType();
    visitParameterList(t, n, fnType);
    ensureTyped(t, n, fnType.getInstanceType());
  } else if (type.isEmptyType() || type.isUnknownType()) {
    ensureTyped(t, n);
  } else {
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n);
  }
}