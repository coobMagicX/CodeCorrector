private void visitParameterList(NodeTraversal t, Node call,
    FunctionType functionType) {
  Iterator<Node> arguments = call.children().iterator();
  arguments.next(); // skip the function name

  Iterator<Node> parameters = functionType.getParameters().iterator();
  int ordinal = 0;
  Node parameter = null;
  Node argument = null;

  boolean isVarArgs = functionType.isVarArgs();
  while (arguments.hasNext()) {
    argument = arguments.next();
    if (parameters.hasNext()) {
      parameter = parameters.next();
    } else if (!isVarArgs) {
      break; // If not var_args and no parameters left, stop checking
    }
    ordinal++;

    validator.expectArgumentMatchesParameter(t, argument,
        getJSType(argument), isVarArgs ? functionType.getVarArgType() : getJSType(parameter), call, ordinal);
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