private void visitParameterList(NodeTraversal t, Node call, FunctionType functionType) {
    Iterator<Node> arguments = call.children().iterator();
    arguments.next(); // Skip the function name which is the first child

    Iterator<Node> parameters = functionType.getParameters().iterator();
    int ordinal = 0;
    Node argument;
    Node parameter = null;
    boolean allParametersUsed = true;

    // Check each argument against each parameter
    while (arguments.hasNext()) {
        argument = arguments.next();
        if (parameters.hasNext()) {
            parameter = parameters.next();
        } else {
            allParametersUsed = false;
        }
        ordinal++;
        if (parameter != null) {
            validator.expectArgumentMatchesParameter(t, argument, getJSType(argument), getJSType(parameter), call, ordinal);
        }
    }
    
    // If there are more parameters than arguments supplied
    int numArgs = call.getChildCount() - 1; // exclude the function name node
    int numParams = functionType.getMinArguments(); // Assume this returns the number of parameters expected.
    
    // Additional validation if specified max arguments
    // int maxArgs = functionType.getMaxArguments();
    if (numArgs < numParams || !allParametersUsed) {
        reportWrongArgumentCount(t, call, numArgs, numParams);
    }
}

/**
 * Utility method to report the difference in expected and actual argument count.
 */
private void reportWrongArgumentCount(NodeTraversal t, Node call, int numArgs, int expectedArgs) {
    String message = String.format("Incorrect number of arguments. Expected: %d, but got: %d", expectedArgs, numArgs);
    t.report(call, WRONG_ARGUMENT_COUNT, message);
}
