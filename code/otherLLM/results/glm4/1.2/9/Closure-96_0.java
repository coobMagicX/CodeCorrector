private void visitParameterList(NodeTraversal t, Node call,
    FunctionType functionType) {
  Iterator<Node> arguments = call.children().iterator();
  arguments.next(); // skip the function name

  Iterator<Node> parameters = functionType.getParameters().iterator();
  int ordinal = 0;
  Node parameter = null;
  Node argument = null;

  while (arguments.hasNext()) {
    if (!parameters.hasNext()) {
      // If we've run out of parameters but still have arguments,
      // it's a var_args scenario, so no need to continue matching.
      break;
    }
    
    parameter = parameters.next();
    argument = arguments.next();
    ordinal++;

    validator.expectArgumentMatchesParameter(t, argument,
        getJSType(argument), getJSType(parameter), call, ordinal);
  }

  int numArgs = call.getChildCount() - 1; // Adjusted to consider the function name
  int minArgs = functionType.getMinArguments();
  int maxArgs = functionType.getMaxArguments();

  if (minArgs > numArgs) {
    report(t, call, WRONG_ARGUMENT_COUNT,
            validator.getReadableJSTypeName(call.getFirstChild(), false),
            String.valueOf(numArgs), String.valueOf(minArgs));
  } else if (maxArgs < numArgs) {
    report(t, call, WRONG_ARGUMENT_COUNT,
            validator.getReadableJSTypeName(call.getFirstChild(), false),
            String.valueOf(numArgs), String.valueOf(minArgs),
            maxArgs != Integer.MAX_VALUE ?
            " and no more than " + maxArgs + " argument(s)" : "");
  }
}