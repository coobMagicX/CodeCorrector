private void visitParameterList(NodeTraversal t, Node call,
    FunctionType functionType) {
  Iterator<Node> arguments = call.children().iterator();
  arguments.next(); // skip the function name

  List<Parameter> parameters = functionType.getParameters();
  int ordinal = 0;
  if (parameters.isEmpty()) { // Check for empty parameter list
    // If the parameter list is empty, it implies that this must be a var_args function.
    while (arguments.hasNext()) {
      argument = arguments.next();
      ordinal++;

      validator.expectArgumentMatchesParameter(t, argument,
          getJSType(argument), "varargs", call, ordinal); // Use 'varargs' as placeholder type for varargs
    }
  } else {
    Iterator<Parameter> parameterIterator = parameters.iterator();
    while (arguments.hasNext() && parameterIterator.hasNext()) {
      Parameter parameter = parameterIterator.next();
      argument = arguments.next();
      ordinal++;

      validator.expectArgumentMatchesParameter(t, argument,
          getJSType(argument), getJSType(parameter.getType()), call, ordinal);
    }
  }

  int numArgs = call.getChildCount() - 1;
  int minArgs = functionType.getMinArguments();
  int maxArgs = functionType.getMaxArguments();

  // Check if the actual number of arguments is within the expected range
  if (minArgs > numArgs || maxArgs < numArgs) {
    report(t, call, WRONG_ARGUMENT_COUNT,
            validator.getReadableJSTypeName(call.getFirstChild(), false),
            String.valueOf(numArgs), String.valueOf(minArgs),
            maxArgs != Integer.MAX_VALUE ?
            " and no more than " + maxArgs + " argument(s)" : "");
  }
}