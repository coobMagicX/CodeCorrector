private void visitParameterList(NodeTraversal t, Node call,
    FunctionType functionType) {
  Iterator<Node> arguments = call.children().iterator();
  // skip the function name by advancing to the first argument
  if (arguments.hasNext()) {
    arguments.next();
  }

  Iterator<Node> parameters = functionType.getParameters().iterator();
  int ordinal = 0;
  Node parameter = null;
  Node argument = null;

  while (arguments.hasNext() && parameters.hasNext()) {
    parameter = parameters.next();
    argument = arguments.next();
    ordinal++;

    JSType argType = getJSType(argument);
    JSType paramType = getJSType(parameter);

    // Ensure that the types are not null before calling expectArgumentMatchesParameter
    if (argType != null && paramType != null) {
      validator.expectArgumentMatchesParameter(t, argument, argType, paramType, call, ordinal);
    } else {
      // Log an error or take appropriate action for the case where the type is unknown
      compiler.report(t.makeError(call, UNKNOWN_TYPE_ERROR, "Unknown type encountered for parameter at ordinal " + ordinal));
    }
  }

  int numArgs = call.getChildCount() - 1;
  int minArgs = functionType.getMinArguments();
  int maxArgs = functionType.getMaxArguments();

  if (minArgs > numArgs || maxArgs < numArgs) {
    String extraCondition = maxArgs != Integer.MAX_VALUE ?
            " and no more than " + maxArgs + " argument(s)" : "";
    report(t, call, WRONG_ARGUMENT_COUNT,
            validator.getReadableJSTypeName(call.getFirstChild(), false),
            String.valueOf(numArgs), String.valueOf(minArgs), extraCondition);
  }
}