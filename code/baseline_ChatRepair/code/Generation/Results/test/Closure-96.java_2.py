private void visitParameterList(NodeTraversal t, Node call, FunctionType functionType) {
  Iterator<Node> arguments = call.children().iterator();
  arguments.next(); // skip the function name

  Iterator<Node> parameters = functionType.getParameters().iterator();
  int ordinal = 0;
  Node parameter = null;
  Node argument = null;

  while (arguments.hasNext() && parameters.hasNext()) {
    parameter = parameters.next();
    argument = arguments.next();
    ordinal++;

    validator.expectArgumentMatchesParameter(t, argument, getJSType(argument), getJSType(parameter), call, ordinal);
  }

  if (parameters.hasNext() && !arguments.hasNext()) {
    // If there are parameters left but no arguments, then it might be an error
    // based on the function type settings (like missing arguments).
  }

  int numArgs = call.getChildCount() - 1;
  int minArgs = functionType.getMinArguments();
  int maxArgs = functionType.getMaxArguments();

  if (minArgs > numArgs || maxArgs < numArgs) {
    String maxArgsStr = maxArgs == Integer.MAX_VALUE ? "" 
                      : " and no more than " + maxArgs + " argument(s)";
    report(t, call, WRONG_ARGUMENT_COUNT,
           validator.getReadableJSTypeName(call.getFirstChild(), false),
           String.valueOf(numArgs), String.valueOf(minArgs),
           maxArgsStr);
  }
}
