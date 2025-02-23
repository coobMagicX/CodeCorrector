private void visitParameterList(NodeTraversal t, Node call, FunctionType functionType) {
    Iterator<Node> arguments = call.children().iterator();
    arguments.next(); // skip the function name

    List<Node> parameterList = functionType.getParameters();
    Iterator<Node> parameters = parameterList.iterator();
    boolean isVarArgs = parameterList.size() > 0 && parameterList.get(parameterList.size() - 1).isVarArgs(); // Replace isVarArgs() with actual method to check if last parameter supports varargs
    int ordinal = 0;
    Node parameter = null;
    Node argument = null;
    
    while (arguments.hasNext()) {
        if (parameters.hasNext()) {
            parameter = parameters.next();
        } else if (!isVarArgs) {
            break; // If no more parameters and not a varargs function, break the loop
        }

        if (arguments.hasNext()) {
            argument = arguments.next();
            ordinal++;
            if (parameter != null) {
                validator.expectArgumentMatchesParameter(t, argument,
                    getJSType(argument), getJSType(parameter), call, ordinal);
            }
        }
    }

    int numArgs = call.getChildCount() - 1;
    int minArgs = functionType.getMinArguments();
    int maxArgs = functionType.getMaxArguments();

    if (isVarArgs) {
        if (minArgs > numArgs) {
            report(t, call, WRONG_ARGUMENT_COUNT, validator.getReadableJSTypeName(call.getFirstChild(), false), 
                String.valueOf(numArgs), String.valueOf(minArgs), " or more arguments");
        }
    } else {
        if (minArgs > numArgs || maxArgs < numArgs) {
            report(t, call, WRONG_ARGUMENT_COUNT, validator.getReadableJSTypeName(call.getFirstChild(), false),
                String.valueOf(numArgs), String.valueOf(minArgs),
                maxArgs != Integer.MAX_VALUE ? " and no more than " + maxArgs + " argument(s)" : "");
        }
    }
}
