private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
  
  if (type.isConstructor() || type.isEmptyType()) { // Removed '|| type.isUnknownType()' to avoid incorrect handling of interfaces
    FunctionType fnType = type.toMaybeFunctionType();
    if (fnType != null) {
      visitParameterList(t, n, fnType);
      
      // Check if the function type has an interface as its return type
      if (fnType.getReturnType().isInterface()) {
        InterfaceType intFnType = fnType.getReturnType().toMaybeInterfaceType();
        
        // If it's a constructor type with an interface as its return type, 
        // we should only ensure typed with the instance type of the function type
        if (intFnType != null) {
          ensureTyped(t, n, intFnType.getInstanceType());
        } else {
          ensureTyped(t, n);
        }
      } else {
        ensureTyped(t, n, fnType.getInstanceType());
      }
    } else {
      ensureTyped(t, n);
    }
  } else {
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n);
  }
}