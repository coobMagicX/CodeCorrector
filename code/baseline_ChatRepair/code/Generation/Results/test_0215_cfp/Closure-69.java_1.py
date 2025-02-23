private void visitCall(NodeTraversal t, Node n) {
  Node child = n.getFirstChild();
  JSType childType = getJSType(child).restrictByNotNullOrUndefined();

  if (!childType.canBeCalled()) {
    report(t, n, NOT_CALLABLE, childType.toString());
    ensureTyped(t, n);
    return;
  }

  // A couple of types can be called as if they were functions.
  // If it is a function type, validate parameters.
  if (childType instanceof FunctionType) {
    FunctionType functionType = (FunctionType) childType;

    boolean isExtern = false;
    JSDocInfo functionJSDocInfo = functionType.getJSDocInfo();
    if (functionJSDocInfo != null) {
      String sourceName = functionJSDocInfo.getSourceName();
      CompilerInput functionSource = compiler.getInput(sourceName);
      isExtern = functionSource.isExtern();
    }

    // Non-native constructors should not be called directly unless they specify a return type and are defined in an extern.
    if (functionType.isConstructor() && !functionType.isNativeObjectType() &&
        (functionType.getReturnType().isUnknownType() || 
         functionType.getReturnType().isVoidType() || 
         !isExtern)) {
      report(t, n, CONSTRUCTOR_NOT_CALLABLE, childType.toString());
    }

    // Functions with explicit 'this' types must be called in a GETPROP or GETELEM.
    JSType thisType = functionType.getTypeOfThis();
    boolean properThisUsage = isCalledProperlyWithThis(t, n, thisType);
    if (thisType != null && !thisType.isEmptyType() && !properThisUsage) {
      report(t, n, INVALID_THIS_TYPE_CALL, childType.toString());
    }

    visitParameterList(t, n, functionType);
    ensureTyped(t, n, functionType.getReturnType());
  } else {
    ensureTyped(t, n);
  }
}

// Check if the call is made properly considering 'this' context by checking parent nodes
private boolean isCalledProperlyWithThis(NodeTraversal t, Node n, JSType thisType) {
  Node parent = n.getParent();
  return parent != null && (parent.isGetProp() || parent.isGetElem());
}
