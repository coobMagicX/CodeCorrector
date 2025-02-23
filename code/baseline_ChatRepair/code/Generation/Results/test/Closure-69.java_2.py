private void visitCall(NodeTraversal t, Node n) {
  Node child = n.getFirstChild();
  JSType childType = getJSType(child).restrictByNotNullOrUndefined();

  if (!childType.canBeCalled()) {
    report(t, n, NOT_CALLABLE, childType.toString());
    ensureTyped(t, n);
    return;
  }

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
    if (functionType.getTypeOfThis() != null && !isGetPropOrGetElemCall(n)) {
      report(t, n, NOT_CALLABLE, "Function requires proper 'this' context.");
    }

    visitParameterList(t, n, functionType);
    ensureTyped(t, n, functionType.getReturnType());
  } else {
    ensureTyped(t, n);
  }
}

private boolean isGetPropOrGetElemCall(Node n) {
  // Check if the invocation is through a GETPROP or GETELEM node.
  Node parent = n.getParent();
  return parent != null && (parent.isGetProp() || parent.isGetElem());
}
