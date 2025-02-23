private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
  
  
  if (type != null && (type.isConstructor() || type.isEmptyType() || type.isUnknownType())) {
    
    FunctionType fnType = type.isConstructor() ? type.toMaybeFunctionType() : null;
    
    if (fnType != null) {
      
      visitParameterList(t, n, fnType);
      ensureTyped(t, n, fnType.getInstanceType());
    } else {
      
      ensureTyped(t, n);
    }
  } else {
    