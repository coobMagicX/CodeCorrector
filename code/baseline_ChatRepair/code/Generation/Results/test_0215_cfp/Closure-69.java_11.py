private void visitCall(NodeTraversal t, Node n) {
  Node child = n.getFirstChild();
  JSType childType = getJSType(child).restrictByNotNullOrUndefined();

  if (!childType.canBeCalled()) {
    report(t, n, NOT_CALLABLE, childType.toString());
    ensureTyped(t, n);
    return;
  }

  // If it is a function type, then validate parameters and `this` context.
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
        (functionType.getReturnType().isUnknownType() || functionType.getReturnType().isVoidType() || !isExtern)) {
      report(t, n, CONSTRUCTOR_NOT_CALLABLE, childType.toString());
    }

    // Checking the context for function call if `this` type is specified.
    boolean hasInvalidContext = false;
    JSType typeOfThis = functionType.getTypeOfThis();
    if (typeOfThis != null && typeOfThis.isConstructor() && !typeOfThis.isNativeObjectType()) {
      Node parent = n.getParent();

      // Hypothetically using `isGetProp` and `isGetElem` replacements. Adjust according to actual API.
      hasInvalidContext = !(parent.isCall() && (parent.getFirstChild() == n && (parent.isGetProp() || parent.isGetElem())));

      if (hasInvalidContext) {
        report(t, n, INVALID_THIS_CONTEXT, childType.toString());
      }
    }

    visitParameterList(t, n, functionType);
    ensureTyped(t, n, functionType.getReturnType());
  } else {
    ensureTyped(t, n);
  }
}
