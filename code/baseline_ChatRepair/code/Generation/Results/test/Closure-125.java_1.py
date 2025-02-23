private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor);
  
  // Add a null check for type and for restrictByNotNullOrUndefined
  if (type != null) {
    type = type.restrictByNotNullOrUndefined();
  } else {
    report(t, n, INVALID_TYPE);
    ensureTyped(t, n);
    return;
  }

  if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
    FunctionType fnType = type.toMaybeFunctionType();
    if (fnType != null) {
      visitParameterList(t, n, fnType);
      JSType instanceType = fnType.getInstanceType();
      // Check if instanceType is not null before ensuring its type
      if (instanceType != null) {
        ensureTyped(t, n, instanceType);
      } else {
        report(t, n, INVALID_INSTANCE_TYPE);
        ensureTyped(t, n);
      }
    } else {
      report(t, n, TYPE_NOT_CALLABLE);
      ensureTyped(t, n);
    }
  } else {
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n);
  }
}
