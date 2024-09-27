public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        // Handle vararg capture, assuming all varargs are captured at once
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                if (position == indexOfVararg && isVarargMatcher(m)) {
                    Object array = invocation.getRawArguments()[position];
                    for (int i = 0; i < Array.getLength(array); i++) {
                        ((CapturesArguments) m).captureFrom(Array.get(array, i));
                    }
                } else {
                    ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
                }
            }
        }
    } else {
        // Process non-vararg arguments
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }
    }
}

private boolean isVarargMatcher(Matcher matcher) {
    // Assume this method returns true if the matcher can handle varargs
    return matcher instanceof VarargMatcher;
}