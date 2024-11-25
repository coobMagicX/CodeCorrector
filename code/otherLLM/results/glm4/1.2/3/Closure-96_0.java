private void visitParameterList(NodeTraversal t, Node call, FunctionType functionType) {
  Iterator<Node> arguments = call.children().iterator();
  arguments.next(); // skip the function name

  Iterator<Node> parameters = functionType.getParameters().iterator();
  int ordinal = 0;
  while (parameters.hasNext()) { // Changed from 'while (arguments.hasNext() && parameters.hasNext())'
    if (!arguments.hasNext()) {
      break; // No more arguments, exit the loop
    }
    
    Node parameter = parameters.next();
    Node argument = arguments.next();
    ordinal++;

    JSType argType = getJSType(argument);
    JSType paramType = getJSType(parameter);

    if (argType == null || !argType.isNumber() || paramType == null || !paramType.isNumber()) {
      report(t, argument, ARGUMENT_TYPE_MISMATCH, argType.toString(), paramType.toString());
      continue; // Invalid type for var_args, skip current iteration
    }

    validator.expectArgumentMatchesParameter(t, argument, getJSType(argument), getJSType(parameter),
        call, ordinal);
  }

  int numArgs = call.getChildCount() - 1;
  int minArgs = functionType.getMinArguments();
  int maxArgs = functionType.getMaxArguments();
  if (minArgs > numArgs || (maxArgs != Integer.MAX_VALUE && maxArgs < numArgs)) {
    report(t, call, WRONG_ARGUMENT_COUNT,
        validator.getReadableJSTypeName(call.getFirstChild(), false),
        String.valueOf(numArgs), String.valueOf(minArgs),
        maxArgs != Integer.MAX_VALUE ?
        " and no more than " + maxArgs + " argument(s)" : "");
  }
}

private void report(NodeTraversal t, Node n, String code, Object... args) {
  compiler.report(t.makeError(n, code, args));
}