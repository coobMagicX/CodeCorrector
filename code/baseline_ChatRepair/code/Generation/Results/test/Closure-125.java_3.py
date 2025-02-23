private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
  
  // Additional log or error handling could be added here if type is not what is expected.
  if (type == null || !type.isConstructor() && !type.isEmptyType() && !type.isUnknownType()) {
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n);
    return; // Add early return to prevent further processing when type is invalid
  }
  
  // Code reaches here means type must be constructor, empty or unknown
  FunctionType fnType = type.toMaybeFunctionType();
  if (fnType != null) {
    visitParameterList(t, n, fnType);
    ensureTyped(t, n, fnType.getInstanceType());
  } else {
    // This ensures that we handle null function types gracefully
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n);
  }
}
