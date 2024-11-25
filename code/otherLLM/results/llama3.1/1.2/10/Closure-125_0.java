private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
  if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
    FunctionType fnType = type.toMaybeFunctionType();
    if (fnType != null) {
      visitParameterList(t, n, fnType);
      ensureTyped(t, n, getJSType(constructor).restrictByNotNullOrUndefined().getInstanceType());
    } else {
      report(t, n, NOT_A_CONSTRUCTOR);
      ensureTyped(t, n);
    }
  } else if (constructor != null) {
    report(t, n, CONSTRUCTOR_EQUALITY_CHECKS);
    visitParameterList(t, n, getJSType(constructor).restrictByNotNullOrUndefined());
    ensureTyped(t, n, getJSType(constructor).restrictByNotNullOrUndefined().getInstanceType());
  } else if (fnType != null) {
    report(t, n, TYPE_CHECKING_FUNCTION_TYPES);
    visitParameterList(t, n, fnType);
    ensureTyped(t, n, fnType.getInstanceType());
  }
}