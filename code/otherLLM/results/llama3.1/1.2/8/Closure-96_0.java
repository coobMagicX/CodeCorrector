private void visitParameterList(NodeTraversal t, Node call,
    FunctionType functionType) {
  Iterator<Node> arguments = call.children().iterator();
  arguments.next(); // skip the function name

  Iterator<Node> parameters = functionType.getParameters().iterator();
  int ordinal = 0;
  Node parameter = null;
  Node argument = null;

  while (arguments.hasNext()) { // <--- CHANGED HERE
    if (!parameters.hasNext()) {
      // If there are no more parameters, then the while loop implies that this must be a var_args function.
      report(t, call, WRONG_ARGUMENT_COUNT,
              validator.getReadableJSTypeName(call.getFirstChild(), false),
              String.valueOf(arguments.next().getChildCount()), "0",
              Integer.MAX_VALUE);
    } else {
      parameter = parameters.next();
      argument = arguments.next();
      ordinal++;

      validator.expectArgumentMatchesParameter(t, argument,
          getJSType(argument), getJSType(parameter), call, ordinal);

      if (ordinal > functionType.getMaxArguments()) { // <--- ADDED THIS CHECK
        report(t, call, WRONG_ARGUMENT_COUNT,
                validator.getReadableJSTypeName(call.getFirstChild(), false),
                String.valueOf(arguments.next().getChildCount()), "0",
                Integer.MAX_VALUE);
        break;
      }
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