private void visitParameterList(NodeTraversal t, Node call,
    FunctionType functionType) {
  Iterator<Node> arguments = call.children().iterator();
  arguments.next(); // skip the function name

  Iterator<Node> parameters = functionType.getParameters().iterator();
  int ordinal = 0;
  Node parameter = null;
  Node argument = null;

  while (arguments.hasNext()) {
    if (!parameters.hasNext() && functionType.isVarArgs()) {
      // Handle varargs by allowing multiple types for the last parameter
      break;
    }
    
    parameter = parameters.next();
    argument = arguments.next();
    ordinal++;

    String argType = getJSType(argument);
    String paramType = getJSType(parameter);

    if (!argType.equals(paramType)) {
      report(t, call, MISMATCHED_ARGUMENT_TYPES,
              validator.getReadableJSTypeName(call.getFirstChild(), false),
              argType, paramType);
      return; // Stop processing further arguments
    }
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