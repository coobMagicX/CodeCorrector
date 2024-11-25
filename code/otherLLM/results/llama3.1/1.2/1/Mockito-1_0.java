public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        throw new UnsupportedOperationException();

    } else {
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }

        // Check if there are any variable arguments that haven't been captured yet
        for (int position = 0; position < invocation.getRawArguments().length; position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments && isVariableArgument(invocation, position) && isVarargMatcher(m)) {
                // If it's a variable argument and the matcher is a vararg matcher, capture the argument
                ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[position]);
            }
        }
    }
}