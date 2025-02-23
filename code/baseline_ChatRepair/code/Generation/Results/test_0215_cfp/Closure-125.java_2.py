private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor);

  if (type != null) {
    type = type.restrictByNotNullOrUndefined();
  } else {
    // Default handling if `type` is null, to ensure safety
    ensureTyped(t, n);
    return;
  }

  if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
    FunctionType fnType = type.toMaybeFunctionType();
    if (fnType != null) {
      // Ensure fnType is not null before accessing its methods
      visitParameterList(t, n, fnType); 
      JSType instanceType = fnType.getInstanceType();
      if (instanceType != null) {
        ensureTyped(t, n, instanceType);
      } else {
        // Handle case when instanceType is null
        ensureTyped(t, n);
      }
    } else {
      // Handle case when fnType conversion is null
      ensureTyped(t, n);
    }
  } else {
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n);
  }
}
