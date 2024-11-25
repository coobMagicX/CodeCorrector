private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
  
  if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
    FunctionType fnType = type.toMaybeFunctionType();
    
    // Check for multiple constructors with the same interface
    if (fnType != null && constructor.hasNextSibling() && 
        getJSType(constructor.getNextSibling()).equals(type)) {
      report(t, n, CONSTRUCTOR_INTERFACE_AMBIGUITY);
    } else {
      visitParameterList(t, n, fnType);
      ensureTyped(t, n, fnType.getInstanceType());
    }
  } else {
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n);
  }
}