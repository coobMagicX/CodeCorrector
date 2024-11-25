public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        // We are not throwing an UnsupportedOperationException here because we want to handle varargs.
        for (int position = 0; position <= indexOfVararg; position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }

    } else {
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments && position < invocation.getRawArguments().length) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }
    }

    // We already handled varargs in the first block. This loop should only handle non-vararg arguments.
    for (int position = 0; position < matchers.size(); position++) {
        Matcher m = matchers.get(position);
        if (m instanceof CapturesArguments && !invocation.getMethod().isVarArgs()) { // Check to make sure it's not a vararg method
            Object argument = invocation.getRawArguments()[position];
            if (argument != null) {
                ((CapturesArguments) m).captureFrom(argument);
            }
        }
    }
}