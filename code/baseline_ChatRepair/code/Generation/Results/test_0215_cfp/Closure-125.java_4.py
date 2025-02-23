private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
  
  // Check if constructor type is either a valid constructor or not.
  if (type == null || (!type.isConstructor() && !type.isEmptyType() && !type.isUnknownType())) {
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n);
  } else {
    // Assuming type being valid constructor or known unknown/empty type.
    FunctionType fnType = type.toMaybeFunctionType();
    if (fnType != null) {
      visitParameterList(t, n, fnType);
      ensureTyped(t, n, fnType.getInstanceType());
    } else {
      ensureTyped(t, n, type); // fallback to original type if conversion to FunctionType fails
    }
  }
}
