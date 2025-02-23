public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        Object array = invocation.getRawArguments()[indexOfVararg];
        for (int position = 0; position < matchers.size() - 1; position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }
        // Handle vararg at the last position, can be multiple elements
        Matcher varargMatcher = matchers.get(matchers.size() - 1);
        if (varargMatcher instanceof CapturesArguments) {
            for (int i = 0; i < Array.getLength(array); i++) {
                ((CapturesArguments) varargMatcher).captureFrom(Array.get(array, i));
            }
        }
    } else {
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }
    }
}
