private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
  
  
  if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
    FunctionType fnType = type.toMaybeFunctionType();
    
    
    if (fnType != null && fnType.isConstructor()) {
      visitParameterList(t, n, fnType);
      ensureTyped(t, n, fnType.getInstanceType());
    } else {
      
      report(t, n, NOT_A_VALID_FUNCTION_TYPE);
      ensureTyped(t, n);
    }
  } else {
    report(t, n