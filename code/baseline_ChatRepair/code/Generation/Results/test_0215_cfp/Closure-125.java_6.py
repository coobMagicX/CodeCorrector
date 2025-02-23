private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
  
  if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
    // Ensure that type can be converted to a FunctionType if not null
    FunctionType fnType = type.toMaybeFunctionType();
    
    // For safety, check both if fnType is not null and can indeed be used as a constructor
    if (fnType != null && fnType.isConstructor()) {
      visitParameterList(t, n, fnType);
      ensureTyped(t, n, fnType.getInstanceType());
    } else {
      ensureTyped(t, n); // Default typing if function type is unsuitable
    }
  } else {
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n);
  }
}
