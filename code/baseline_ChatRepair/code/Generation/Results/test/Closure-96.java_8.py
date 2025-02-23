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

    validator.expectArgumentMatchesParameter(t, argument,
        getJSType(argument), getJSType(parameter), call, ordinal);
  }

  // Collect remaining arguments if there are more than parameters expected
  while (arguments.hasNext()) {
    argument = arguments.next();
    ordinal++;
    // No corresponding parameter for this argument can be located
    validator.expectArgumentMatchesParameter(t, argument, 
        getJSType(argument), null, call, ordinal);
  }

  int numArgs = call.getChildCount() - 1;
  int minArgs = functionType.getMinArguments();
  int maxArgs = functionType.getMaxArguments();
  // Check against minimum and maximum arguments if maxArgs is not unlimited
  if ((minArgs > numArgs || (maxArgs < numArgs && maxArgs != Integer.MAX_VALUE))) {
    report(t, call, WRONG_ARGUMENT_COUNT,
            validator.getReadableJSTypeName(call.getFirstChild(), false),
            String.valueOf(numArgs), String.valueOf(minArgs),
            maxArgs != Integer.MAX_VALUE ? " and no more than " + maxArgs + " arguments" : " or more as needed");
  }
}
