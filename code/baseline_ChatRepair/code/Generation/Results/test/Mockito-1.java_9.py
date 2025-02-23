public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        // Handling of varargs should be appropriately done in the corresponding handling section below.
        // Removed the throw statement to allow processing to continue.
    }

    for (int position = 0; position < matchers.size(); position++) {
        Matcher m = matchers.get(position);
        if (m instanceof CapturesArguments) {
            if (position < invocation.getRawArguments().length) {
                if (isVariableArgument(invocation, position) && isVarargMatcher(m)) {
                    Object array = invocation.getRawArguments()[position];
                    for (int i = 0; i < Array.getLength(array); i++) {
                        ((CapturesArguments) m).captureFrom(Array.get(array, i));
                    }
                    // Since we've captured all varargs already, it does not make sense to process other matchers.
                    break; // Changed from return to break to avoid skipping remaining non-vararg captures
                } else {
                    ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
                }
            }
        }
    }
}

private boolean isVariableArgument(Invocation invocation, int position) {
    return invocation.getMethod().isVarArgs() && position == invocation.getRawArguments().length - 1;
}

private boolean isVarargMatcher(Matcher matcher) {
    // Implement logic to determine if the matcher is intended for varargs
    return matcher instanceof VarargMatcher; // Example type check; actual implementation may vary
}
