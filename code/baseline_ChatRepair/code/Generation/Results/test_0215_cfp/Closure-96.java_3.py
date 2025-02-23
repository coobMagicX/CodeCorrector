private void visitParameterList(NodeTraversal t, Node call, FunctionType functionType) {
    Iterator<Node> arguments = call.children().iterator();
    arguments.next(); // skip the function name

    Iterator<Node> parameters = functionType.getParameters().iterator();
    int ordinal = 0;
    Node parameter = null;
    Node argument = null;
    boolean isVarArgs = functionType.isVarArgs();

    // Loop through each argument and match with parameters
    while (arguments.hasNext()) {
        argument = arguments.next();
        if (parameters.hasNext()) {
            parameter = parameters.next();
        }
        ordinal++; // Increment the ordinal here to reflect correct arg position

        if (parameter != null) { // check if still parameter exists to validate type
            JSType argumentType = getJSType(argument);
            JSType parameterType = getJSType(parameter);
            validator.expectArgumentMatchesParameter(t, argument, argumentType, parameterType, call, ordinal);
        } else if (!isVarArgs) {
            // No parameter corresponding to this argument and not a var_args function
            reportExtraArgument(t, call, argument, ordinal);
            break; // report first excess argument only
        }
    }

    int numArgs = call.getChildCount() - 1;
    int minArgs = functionType.getMinArguments();
    int maxArgs = functionType.getMaxArguments();

    if (numArgs < minArgs || (numArgs > maxArgs && !isVarArgs)) {
        report(t, call, WRONG_ARGUMENT_COUNT,
               validator.getReadableJSTypeName(call.getFirstChild(), false),
               String.valueOf(numArgs), String.valueOf(minArgs),
               maxArgs != Integer.MAX_VALUE ? " and no more than " + maxArgs + " argument(s)" : "");
    }
}

private void reportExtraArgument(NodeTraversal t, Node call, Node argument, int ordinal) {
    // Simplified error reporting (you may want to refine message and error code)
    report(t, argument, WRONG_ARGUMENT_COUNT, "Extra argument at position " + ordinal);
}
