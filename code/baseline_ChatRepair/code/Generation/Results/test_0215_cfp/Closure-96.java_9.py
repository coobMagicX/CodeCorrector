private void visitParameterList(NodeTraversal t, Node call, FunctionType functionType) {
    Iterator<Node> arguments = call.children().iterator();
    arguments.next(); // skip the function name

    Iterator<Node> parameters = functionType.getParameters().iterator();
    int ordinal = 0;
    Node parameter = null;
    Node argument = null;
    boolean isVarArgs = functionType.isVarArgs();
    while (arguments.hasNext() && (parameters.hasNext() || isVarArgs)) {
        if (parameters.hasNext()) {
            parameter = parameters.next();
        }
        argument = arguments.next();
        ordinal++;

        // Validate only if parameter exists; skip validation for extra var_args
        if (parameter != null) {
            validator.expectArgumentMatchesParameter(t, argument,
                getJSType(argument), getJSType(parameter), call, ordinal);
        }
    }

    int numArgs = call.getChildCount() - 1;
    int minArgs = functionType.getMinArguments();
    int maxArgs = functionType.getMaxArguments();
    if (isVarArgs) {
        // For var_args, only check minimum arguments
        if (minArgs > numArgs) {
            report(t, call, WRONG_ARGUMENT_COUNT,
                    validator.getReadableJSTypeName(call.getFirstChild(), false),
                    String.valueOf(numArgs), String.valueOf(minArgs), " or more arguments");
        }
    } else if (minArgs > numArgs || maxArgs < numArgs) {
        // Standard argument count check when not var_args
        report(t, call, WRONG_ARGUMENT_COUNT,
                validator.getReadableJSTypeName(call.getFirstChild(), false),
                String.valueOf(numArgs), String.valueOf(minArgs),
                maxArgs != Integer.MAX_VALUE ?
                " and no more than " + maxArgs + " argument(s)" : "");
    }
}
