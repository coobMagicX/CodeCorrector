private void visitParameterList(NodeTraversal t, Node call, FunctionType functionType) {
  Iterator<Node> arguments = call.children().iterator();
  arguments.next(); // skip the function name

  Iterator<Node> parameters = functionType.getParameters().iterator();
  int ordinal = 0;
  Node parameter = null;
  Node argument = null;
  while (arguments.hasNext() && (parameters.hasNext() || functionType.isVarArgs())) {
    parameter = parameters.hasNext() ? parameters.next() : parameter; // maintain the last parameter for varargs
    argument = arguments.next();
    ordinal++;

    validator.expectArgumentMatchesParameter(t, argument, getJSType(argument), getJSType(parameter), call, ordinal);
  }

  int numArgs = call.getChildCount() - 1; // number of actual arguments passed
  int minArgs = functionType.getMinArguments(); // minimum number of arguments expected
  int maxArgs = functionType.getMaxArguments(); // maximum number of arguments expected
  boolean isVarArgs = functionType.isVarArgs(); // whether the function accepts variable number of arguments
  
  // Handle checking for varargs functions.
  if ((minArgs > numArgs) || (!isVarArgs && maxArgs < numArgs)) {
    report(t, call, WRONG_ARGUMENT_COUNT,
      validator.getReadableJSTypeName(call.getFirstChild(), false),
      String.valueOf(numArgs), String.valueOf(minArgs),
      (maxArgs == Integer.MAX_VALUE || isVarArgs) ? " or more arguments as required" : " and no more than " + maxArgs + " arguments");
  }
}
