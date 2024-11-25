public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        // Ensure that the varargs section is processed correctly before throwing exception
        processVarArgs(invocation, indexOfVararg);
        throw new UnsupportedOperationException("Varargs are not supported in this context.");

    } else {
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }
    }

    // Process the remaining matchers if there are no varargs or after processing varargs
    for (int position = 0; position < matchers.size(); position++) {
        Matcher m = matchers.get(position);
        if (m instanceof CapturesArguments && invocation.getRawArguments().length > position) {
            int argIndex = position;
            if (isVariableArgument(invocation, argIndex) && isVarargMatcher(m)) {
                Object array = invocation.getRawArguments()[position];
                for (int i = 0; i < Array.getLength(array); i++) {
                    ((CapturesArguments) m).captureFrom(Array.get(array, i));
                }
            } else {
                ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[argIndex]);
            }
        }
    }
}

private void processVarArgs(Invocation invocation, int indexOfVararg) {
    Object[] varargs = (Object[]) invocation.getRawArguments()[indexOfVararg];
    for (int i = 0; i < varargs.length; i++) {
        Matcher m = matchers.get(indexOfVararg + i);
        if (m instanceof CapturesArguments) {
            ((CapturesArguments) m).captureFrom(varargs[i]);
        }
    }
}