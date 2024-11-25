private void visitParameterList(NodeTraversal t, Node call,
    FunctionType functionType) {
  Iterator<Node> arguments = call.children().iterator();
  arguments.next(); // skip the function name

  Iterator<Parameter> parametersIterator = functionType.getParameters().iterator();

  int ordinal = 0;
  while (arguments.hasNext()) {
    Parameter parameter = null;

    if (parametersIterator.hasNext()) {
      parameter = parametersIterator.next();
    }

    Node argument = arguments.next();
    ordinal++;

    String paramJSType = getJSType(parameter);
    String argJSType = getJSType(argument);

    // Check if the current parameter is a vararg
    boolean isVarArg = (parameter != null) && parameter.isRestParameter();

    if (isVarArg || (parameter == null)) {
      // For varargs or additional arguments, we expect all to be 'number'
      validator.expectArgumentIsOfType(t, argument, "number", call, ordinal);
    } else {
      validator.expectArgumentMatchesParameter(t, argument, argJSType, paramJSType, call, ordinal);
    }
  }

  int numArgs = call.getChildCount() - 1;
  int minArgs = functionType.getMinArguments();
  int maxArgs = functionType.getMaxArguments();

  if (minArgs > numArgs || (maxArgs != Integer.MAX_VALUE && maxArgs < numArgs)) {
    report(t, call, WRONG_ARGUMENT_COUNT,
            validator.getReadableJSTypeName(call.getFirstChild(), false),
            String.valueOf(numArgs), String.valueOf(minArgs),
            maxArgs != Integer.MAX_VALUE ?
            " and no more than " + maxArgs + " argument(s)" : "");
  }
}