private void visitParameterList(NodeTraversal t, Node call, FunctionType functionType) {
  Iterator<Node> arguments = call.children().iterator();
  arguments.next(); // skip the function name

  Iterator<Node> parameters = functionType.getParameters().iterator();
  
  int ordinal = 0;
  Node parameter = null;
  Node argument = null;
  boolean isVarArgs = false;

  // Check all parameters and arguments
  while (arguments.hasNext()) {
    if (parameters.hasNext()) {
      parameter = parameters.next();
      // Assuming that some property of the parameter can indicate it being vararg
      isVarArgs = parameter.isVarArgs(); // Adjust this line based on your actual method to determine if it's a vararg.
    } else if (!isVarArgs) {
      break;
    }

    argument = arguments.next();
    ordinal++;
    validator.expectArgumentMatchesParameter(t, argument, getJSType(argument), getJSType(parameter), call, ordinal);
  }

  int numArgs = call.getChildCount() - 1; // number of actual arguments passed
  int minArgs = functionType.getMinArguments(); // minimum number of arguments expected
  int maxArgs = functionType.getMaxArguments(); // maximum number of arguments expected

  // Check for mismatched argument counts
  if ((minArgs > numArgs) || (!isVarArgs && maxArgs < numArgs)) {
    report(t, call, WRONG_ARGUMENT_COUNT,
      validator.getReadableJSTypeName(call.getFirstChild(), false),
      String.valueOf(numArgs), String.valueOf(minArgs),
      maxArgs != Integer.MAX_VALUE ? " and no more than " + maxArgs + " arguments" : " or more");
  }
}
