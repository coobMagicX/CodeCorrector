private void visitParameterList(NodeTraversal t, Node call,
    FunctionType functionType) {
  Iterator<Node> arguments = call.children().iterator();
  arguments.next(); // skip the function name

  List<FunctionParam> parameters = functionType.getParameters();
  int ordinal = 0;
  for (Node parameter : parameters) {
    if (arguments.hasNext()) {
      Node argument = arguments.next();
      validator.expectArgumentMatchesParameter(t, argument,
          getJSType(argument), getJSType(parameter), call, ordinal);
    } else {
      // This is to handle the var_args function case.
      if (functionType.isVarArgs() && parameters.size() - 1 == ordinal) {
        break;
      }
      report(t, call, ARGUMENT_COUNT_MISMATCH,
              validator.getReadableJSTypeName(parameter, false),
              "None", String.valueOf(ordinal), "");
    }
    ordinal++;
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