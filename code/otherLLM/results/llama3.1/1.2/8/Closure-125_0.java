private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
  
  // Improved constructor identification and function type handling
  if (type.isConstructor()) { 
    FunctionType fnType = type.toMaybeFunctionType(); 
    if (fnType != null) {
      visitParameterList(t, n, fnType);
      ensureTyped(t, n, fnType.getInstanceType());
    } else {
      ensureTyped(t, n);
    }
  } else if (type.isEmptyType() || type.isUnknownType()) { // Added check for empty and unknown types
    FunctionType fnType = type.toMaybeFunctionType();
    if (fnType != null) {
      visitParameterList(t, n, fnType);
      ensureTyped(t, n, fnType.getInstanceType());
    } else {
      ensureTyped(t, n);
    }
  } else { 
    report(t, n, NOT_A_CONSTRUCTOR); // Report error for non-constructors
    ensureTyped(t, n);
  }
}