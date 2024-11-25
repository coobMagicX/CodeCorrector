private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
  
  if (type.isFunctionType()) {
    FunctionType fnType = type.toMaybeFunctionType();
    if (fnType != null) {
      visitParameterList(t, n, fnType);
      ensureTyped(t, n, fnType.getInstanceType());
    } else {
      report(t, n, NOT_A_CONSTRUCTOR);
      ensureTyped(t, n);
    }
  } else if (type.isConstructor()) {
    FunctionType constructorFnType = type.toMaybeFunctionType();
    if (constructorFnType != null) {
      visitParameterList(t, n, constructorFnType);
      ensureTyped(t, n, constructorFnType.getInstanceType());
    } else {
      report(t, n, NOT_A_CONSTRUCTOR);
      ensureTyped(t, n);
    }
  } else {
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n);
  }
}