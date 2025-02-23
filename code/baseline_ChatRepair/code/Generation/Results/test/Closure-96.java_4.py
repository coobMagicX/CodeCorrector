private void visitParameterList(NodeTraversal t, Node call, FunctionType functionType) {
    Iterator<Node> arguments = call.children().iterator();
    arguments.next(); // skip the function name

    Iterator<Node> parameters = functionType.getParameters().iterator();
    int ordinal = 0;
    Node parameter = null;
    Node argument = null;
    boolean hasExcessArguments = false;

    while (arguments.hasNext()) {
        argument = arguments.next();
        if (parameters.hasNext()) {
            parameter = parameters.next();
        } else {
            // If no more parameters and we still have arguments, report excess arguments.
            hasExcessArguments = true;
            break;
        }
        ordinal++;
        validator.expectArgumentMatchesParameter(t, argument, 
            getJSType(argument), getJSType(parameter), call, ordinal);
    }

    // Check if there are still parameters left with no matching arguments
    while (!hasExcessArguments && parameters.hasNext()) {
        parameters.next();
        ordinal++;
        reportMissingArgument(t, call, ordinal);
    }

    int numArgs = call.getChildCount() - 1;
    int minArgs = functionType.getMinArguments();
    int maxArgs = functionType.getMaxArguments();

    if (minArgs > numArgs || (maxArgs < numArgs && !functionType.hasOptionalArgs())) {
        reportWrongArgumentCount(t, call, numArgs, minArgs, maxArgs);
    }

    if (hasExcessArguments) {
        reportExcessArguments(t, call, numArgs, ordinal);
    }
}

private void reportMissingArgument(NodeTraversal t, Node call, int ordinal) {
    t.report(call, MISSING_ARGUMENT, Integer.toString(ordinal));
}

private void reportWrongArgumentCount(NodeTraversal t, Node call, int numArgs, int minArgs, int maxArgs) {
    t.report(call, WRONG_ARGUMENT_COUNT, Integer.toString(numArgs), Integer.toString(minArgs), Integer.toString(maxArgs));
}

private void reportExcessArguments(NodeTraversal t, Node call, int numArgs, int ordinal) {
    t.report(call, EXCESS_ARGUMENTS, Integer.toString(numArgs), Integer.toString(ordinal));
}
