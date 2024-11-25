private void visitCall(NodeTraversal t, Node n) {
  Node child = n.getFirstChild();
  JSType childType = getJSType(child).restrictByNotNullOrUndefined();

  if (!childType.canBeCalled()) {
    report(t, n, NOT_CALLABLE, childType.toString());
    ensureTyped(t, n);
    return;
  }

  // A couple of types can be called as if they were functions.
  // If it is a function type, then validate parameters.
  if (childType instanceof FunctionType) {
    FunctionType functionType = (FunctionType) childType;

    boolean isExtern = false;
    JSDocInfo functionJSDocInfo = functionType.getJSDocInfo();
    if(functionJSDocInfo != null) {
      String sourceName = functionJSDocInfo.getSourceName();
      CompilerInput functionSource = compiler.getInput(sourceName);
      isExtern = functionSource.isExtern();
    }

    // Non-native constructors should not be called directly
    // unless they specify a return type and are defined
    // in an extern.
    if (functionType.isConstructor() &&
        !functionType.isNativeObjectType() &&
        (functionType.getReturnType().isUnknownType() ||
         functionType.getReturnType().isVoidType() ||
         !isExtern)) {
      report(t, n, CONSTRUCTOR_NOT_CALLABLE, childType.toString());
    }

    // Functions with explicit 'this' types must be called in a GETPROP
    // or GETELEM.
    if (functionType.hasExplicitThisType()) {
      Node parent = n.getParent();
      if (!(parent instanceof GetProp || parent instanceof GetElem)) {
        report(t, n, INVALID_THIS_TYPE_CALL, childType.toString());
      }
    }

    visitParameterList(t, n, functionType);
    ensureTyped(t, n, functionType.getReturnType());

    // Add a check for RegExp object calls
    if (childType.isRegExpObjectType()) {
      report(t, n, REGEXP_OBJECT_CALL, childType.toString());
    }
  } else {
    ensureTyped(t, n);
  }

  // TODO: Remove this comment once the issue is fixed
  // TODO: Add something to check for calls of RegExp objects, which is not
  // supported by IE.  Either say something about the return type or warn
  // about the non-portability of the call or both.
}