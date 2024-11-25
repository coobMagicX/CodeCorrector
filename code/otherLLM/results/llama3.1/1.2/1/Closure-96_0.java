private void visitParameterList(NodeTraversal t, Node call,
    FunctionType functionType) {
  Iterator<Node> arguments = call.children().iterator();
  arguments.next(); // skip the function name

  Iterator<Node> parameters = functionType.getParameters().iterator();
  int ordinal = 0;
  Node parameter = null;
  while (parameters.hasNext()) { // Iterate over the declared parameters
    parameter = parameters.next();

    if (functionType.isVarArgs() && !parameter.hasChildren()) { // Check for var_args functions
      continue; // Skip this iteration and move to the next argument
    }

    Node argument = arguments.next();
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