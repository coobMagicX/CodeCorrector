private void visitParameterList(NodeTraversal t, Node call, FunctionType functionType) {
  Iterator<Node> arguments = call.children().iterator();
  arguments.next(); // skip the function name

  Iterator<ParameterType> parametersIterator = functionType.getParameters().iterator();

  int ordinal = 0;
  while (arguments.hasNext() || parametersIterator.hasNext()) { // Ensure we check for varargs
    if (parametersIterator.hasNext()) {
      ParameterType parameterType = parametersIterator.next();
      Node argument = arguments.next();
      validator.expectArgumentMatchesParameter(t, argument,
          getJSType(argument), parameterType.getType(), call, ordinal);
      ordinal++;
    } else { // This is the case for varargs
      if (arguments.hasNext()) {
        report(t, call, DiagnosticType.WRONG_ARGUMENT_COUNT,
                validator.getReadableJSTypeName(call.getFirstChild(), false),
                String.valueOf(functionType.getMinArguments() + ordinal - 1),
                String.valueOf(functionType.getMinArguments()), String.valueOf(Integer.MAX_VALUE));
      }
    }
  }

  int numArgs = call.getChildCount() - 1;
  int minArgs = functionType.getMinArguments();
  int maxArgs = functionType.getMaxArguments();

  if (minArgs > numArgs) {
    report(t, call, DiagnosticType.WRONG_ARGUMENT_COUNT,
            validator.getReadableJSTypeName(call.getFirstChild(), false),
            String.valueOf(numArgs), String.valueOf(minArgs), "");
  } else if (maxArgs < numArgs || maxArgs != Integer.MAX_VALUE && numArgs > maxArgs) {
    report(t, call, DiagnosticType.WRONG_ARGUMENT_COUNT,
            validator.getReadableJSTypeName(call.getFirstChild(), false),
            String.valueOf(numArgs), minArgs + " and no more than " + maxArgs + " argument(s)",
            "");
  }
}