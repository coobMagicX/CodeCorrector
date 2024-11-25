private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();

  // Check if the type is a function or an unknown/empty type
  boolean isFunctionType = type.isConstructor() || type.isEmptyType() || type.isUnknownType();

  if (isFunctionType) {
    FunctionType fnType = type.toMaybeFunctionType();
    if (fnType != null) {
      visitParameterList(t, n, fnType);
      ensureTyped(t, n, fnType.getInstanceType());
    } else {
      // If type is not explicitly a FunctionType but is one of the types we accept, we should still
      // call ensureTyped to handle it properly.
      ensureTyped(t, n);
    }
  } else {
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n); // Ensure typed after reporting error since it might be needed for further processing
  }
}