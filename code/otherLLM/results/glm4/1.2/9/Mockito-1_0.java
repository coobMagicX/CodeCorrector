public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        // Assuming that the UnsupportedOperationException is a placeholder and should be replaced with proper handling of varargs.
        for (int i = 0; i <= indexOfVararg; i++) {
            Matcher m = matchers.get(i);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[i]);
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

    // This loop should handle the case where we have extra raw arguments beyond the expected number of matchers.
    for (int position = 0; position < invocation.getRawArguments().length; position++) {
        Matcher m = matchers.get(position);
        if (m instanceof CapturesArguments) {
            ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[position]);
        }
    }
}