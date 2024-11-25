private void visitParameterList(NodeTraversal t, Node call,
    FunctionType functionType) {
  Iterator<Node> arguments = call.children().iterator();
  arguments.next(); // skip the function name

  Iterator<Node> parameters = functionType.getParameters().iterator();
  int ordinal = 0;
  Node parameter = null;
  Node argument = null;

  while (arguments.hasNext()) {
    if (!parameters.hasNext()) { // If no more parameters, assume var_args.
      break; // Skip the rest of the arguments after the last parameter.
    }

    parameter = parameters.next();
    argument = arguments.next();
    ordinal++;

    validator.expectArgumentMatchesParameter(t, argument,
        getJSType(argument), getJSType(parameter), call, ordinal);
  }

  int numArgs = call.getChildCount() - 1;
  int minArgs = functionType.getMinArguments();
  int maxArgs = functionType.getMaxArguments();

  if (minArgs > numArgs) {
    report(t, call, WRONG_ARGUMENT_COUNT,
            validator.getReadableJSTypeName(call.getFirstChild(), false),
            String.valueOf(numArgs), String.valueOf(minArgs),
            "");
  } else if (maxArgs < numArgs && maxArgs != Integer.MAX_VALUE) {
    // If there is a maximum argument limit and the number of arguments exceeds it
    report(t, call, WRONG_ARGUMENT_COUNT,
            validator.getReadableJSTypeName(call.getFirstChild(), false),
            String.valueOf(numArgs), String.valueOf(minArgs),
            " and no more than " + maxArgs + " argument(s)");
  }
}