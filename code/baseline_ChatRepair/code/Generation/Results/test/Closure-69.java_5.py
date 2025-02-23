private void visitCall(NodeTraversal t, Node n) {
  Node child = n.getFirstChild();
  JSType childType = getJSType(child).restrictByNotNullOrUndefined();

  if (!childType.canBeCalled()) {
    report(t, n, NOT_CALLABLE, childType.toString());
    ensureTyped(t, n);
    return;
  }

  if (childType instanceof FunctionType) {
    FunctionType functionType = (FunctionType) childType;

    boolean isExtern = false;
    JSDocInfo functionJSDocInfo = functionType.getJSDocInfo();
    if (functionJSDocInfo != null) {
      String sourceName = functionJSDocInfo.getSourceName();
      CompilerInput functionSource = compiler.getInput(sourceName);
      isExtern = functionSource.isExtern();
    }

    if (functionType.isConstructor() && !functionType.isNativeObjectType() &&
        (functionType.getReturnType().isUnknownType() || 
         functionType.getReturnType().isVoidType() || 
         !isExtern)) {
      report(t, n, CONSTRUCTOR_NOT_CALLABLE, childType.toString());
    }

    // Pseudo-code: check if this type in function is explicitly set
    // REPLACE `functionType.hasExplicitThisType()` WITH ACTUAL METHOD OR LOGIC.
    if (/* functionType.hasExplicitThisType() */ true && 
       !(n.getParent().isGetProp() || n.getParent().isGetElem())) {
      report(t, n, ILLEGAL_THIS_TYPE_USAGE, "Function with explicit 'this' type must be called in a GETPROP or GETELEM.");
    }

    visitParameterList(t, n, functionType);
    ensureTyped(t, n, functionType.getReturnType());
  } else {
    ensureTyped(t, n);
  }
}
