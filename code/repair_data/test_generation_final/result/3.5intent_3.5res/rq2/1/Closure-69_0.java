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

    // Functions with explcit 'this' types must be called in a GETPROP
    // or GETELEM.

    visitParameterList(t, n, functionType);
    ensureTyped(t, n, functionType.getReturnType());
  } else {
    ensureTyped(t, n);
  }

  // TODO: Add something to check for calls of RegExp objects, which is not
  // supported by IE.  Either say something about the return type or warn
  // about the non-portability of the call or both.
}

private void visitParameterList(NodeTraversal t, Node call, FunctionType functionType) {
  Iterator<Node> arguments = call.children().iterator();
  arguments.next(); // skip the function name

  Iterator<Node> parameters = functionType.getParameters().iterator();
  int ordinal = 0;
  Node parameter = null;
  Node argument = null;
  while (arguments.hasNext() &&
         (parameters.hasNext() ||
          parameter != null && parameter.isVarArgs())) {
    // If there are no parameters left in the list, then the while loop
    // above implies that this must be a var_args function.
    if (parameters.hasNext()) {
      parameter = parameters.next();
    }
    argument = arguments.next();
    ordinal++;

    validator.expectArgumentMatchesParameter(t, argument,
        getJSType(argument), getJSType(parameter), call, ordinal);
  }

  int numArgs = call.getChildCount() - 1;
  int minArgs = functionType.getMinArguments();
  int maxArgs = functionType.getMaxArguments();
  if (minArgs > numArgs || maxArgs < numArgs) {
    report(t, call, WRONG_ARGUMENT_COUNT,
            validator.getReadableJSTypeName(call.getFirstChild(), false),
            String.valueOf(numArgs), String.valueOf(minArgs),
            maxArgs != Integer.MAX_VALUE ?
            " and no more than " + maxArgs + " argument(s)" : "");
  }
}