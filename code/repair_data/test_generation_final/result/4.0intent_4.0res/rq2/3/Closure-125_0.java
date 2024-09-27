private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType constructorType = getJSType(constructor).restrictByNotNullOrUndefined();
  if (constructorType.isConstructor() || constructorType.isEmptyType() || constructorType.isUnknownType()) {
    FunctionType fnType = constructorType.toMaybeFunctionType();
    if (fnType != null) {
      visitParameterList(t, n, fnType);
      ensureTyped(t, n, fnType.getInstanceType());
    } else {
      ensureTyped(t, n);
    }
  } else {
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n);
  }
}