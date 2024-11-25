private void visitParameterList(NodeTraversal t, Node call,
    FunctionType functionType) {
  Iterator<Node> arguments = call.children().iterator();
  arguments.next(); // skip the function name

  Iterator<Node> parameters = functionType.getParameters().iterator();
  int ordinal = 0;
  Node parameter = null;
  Node argument = null;
  while (arguments.hasNext()) {
    if (parameters.hasNext()) {
      parameter = parameters.next();
      argument = arguments.next();
      ordinal++;
      validator.expectArgumentMatchesParameter(t, argument,
          getJSType(argument), getJSType(parameter), call, ordinal);
    } else {
      // Handle variable arguments
      String varArgTypeName = getJSType(arguments.next());
      // Assuming there is a method to check if the last parameter allows for var_args
      if (!functionType.isLastParameterVarArgs()) {
        report(t, call, VAR_ARGS_NOT_ALLOWED,
                validator.getReadableJSTypeName(parameter, false),
                "varargs not allowed");
      } else {
        // If it's a valid var_arg function, ensure the next argument is also processed
        if (arguments.hasNext()) {
          String nextArgTypeName = getJSType(arguments.next());
          // Assuming there is a method to check for type compatibility with the last parameter
          if (!validator.isVarArgTypeCompatible(nextArgTypeName, getJSType(parameter))) {
            report(t, call, TYPE_MISMATCH_IN_VAR_ARGS,
                    validator.getReadableJSTypeName(parameter, false),
                    nextArgTypeName);
          }
        } else {
          // If it's the last argument and not compatible, report a type mismatch
          report(t, call, TYPE_MISMATCH_IN_VAR_ARGS,
                  validator.getReadableJSTypeName(parameter, false),
                  varArgTypeName);
        }
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