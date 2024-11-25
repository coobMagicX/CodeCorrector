public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        throw new UnsupportedOperationException("Varargs are not supported in this context.");

    } else {
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }
    }

    int matcherCount = matchers.size();
    for (int position = 0; position < invocation.getRawArguments().length; position++) {
        if (position >= matcherCount) {
            break; // No more matchers to process
        }
        Matcher m = matchers.get(position);
        if (m instanceof CapturesArguments) {
            Object argument = invocation.getRawArguments()[position];
            ((CapturesArguments) m).captureFrom(argument);

            if (isVariableArgument(invocation, position) && isVarargMatcher(m)) {
                // If this argument is a variable argument and the matcher supports varargs
                Object array = argument;
                for (int i = 0; i < Array.getLength(array); i++) {
                    ((CapturesArguments) m).captureFrom(Array.get(array, i));
                }
            }
        }
    }
}