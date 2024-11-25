private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
  
  // Revised logic for determining whether a node represents a constructor or not
  if (type.isFunctionType() && (type.toMaybeFunctionType().isConstructor() || type.toMaybeFunctionType().isInterface())) {
    FunctionType fnType = type.toMaybeFunctionType();
    visitParameterList(t, n, fnType);
    ensureTyped(t, n, fnType.getInstanceType());
  } else if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
    FunctionType fnType = type.toMaybeFunctionType();
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