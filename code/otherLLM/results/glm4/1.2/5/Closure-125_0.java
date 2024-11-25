private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
  
  // Check if the type is a constructor or if it's an unknown/empty type.
  boolean isConstructorOrUnknown = type.isConstructor() || type.isEmptyType() || type.isUnknownType();
  
  if (isConstructorOrUnknown) {
    FunctionType fnType = type.toMaybeFunctionType();
    
    // If the type is known to be a function, visit the parameter list and ensure typing.
    if (fnType != null) {
      visitParameterList(t, n, fnType);
      ensureTyped(t, n, fnType.getInstanceType());
    } else {
      // If the type is not a function but an unknown/empty type, we still need to ensure typing.
      ensureTyped(t, n);
    }
  } else {
    // If the node is not a constructor, report it as NOT_A_CONSTRUCTOR and ensure typing.
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n);
  }
}