public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        // No need to throw an exception here unless it's required for specific logic.
        // Assuming we should process varargs accordingly, let's proceed:

        // First, handle the non-vararg arguments
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }

        // Now handle the varargs
        Object array = invocation.getRawArguments()[indexOfVararg];
        for (int i = 0; i < Array.getLength(array); i++) {
            ((CapturesArguments) matchers.get(indexOfVararg)).captureFrom(Array.get(array, i));
        }
    } else {
        // If the method is not varargs, simply capture all arguments
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }
    }

    // Ensure we do not process any additional arguments after varargs
    int numberOfMatchers = matchers.size();
    if (invocation.getMethod().isVarArgs()) {
        numberOfMatchers = indexOfVararg + 1;
    }
    for (int position = 0; position < numberOfMatchers; position++) {
        Matcher m = matchers.get(position);
        if (m instanceof CapturesArguments && invocation.getRawArguments().length > position) {
            ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[position]);
        }
    }
}