public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        // Handling variable arguments
        int indexOfVararg = invocation.getRawArguments().length - 1;
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                if (position == indexOfVararg && isVarargMatcher(m)) {
                    Object array = invocation.getRawArguments()[position];
                    for (int i = 0; i < Array.getLength(array); i++) {
                        ((CapturesArguments) m).captureFrom(Array.get(array, i));
                    }
                } else if (position < indexOfVararg) {
                    ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[position]);
                }
            }
        }
    } else {
        // Handling non-variable arguments
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments && invocation.getRawArguments().length > position) {
                ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[position]);
            }
        }
    }
}

private boolean isVarargMatcher(Matcher matcher) {
    // Implement logic to determine if a matcher is meant for varargs
    return matcher instanceof VarArgMatcher; // Placeholder for actual implementation
}