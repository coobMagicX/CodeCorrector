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

        // Added a new check to handle vararg parameters correctly
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments && invocation.getRawArguments().length > position) {
                if(isVariableArgument(invocation, position) && isVarargMatcher(m)) {
                    Object array = invocation.getRawArguments()[position];
                    for (int i = 0; i < Array.getLength(array); i++) {
                        ((CapturesArguments) m).captureFrom(Array.get(array, i));
                    }
                    //since we've captured all varargs already, it does not make sense to process other matchers.
                    return;
                } else if(isVariableArgument(invocation, position)) { 
                    Object array = invocation.getRawArguments()[position];
                    for (int i = 0; i < Array.getLength(array); i++) {
                        ((CapturesArguments) m).captureFrom(Array.get(array, i));
                    }
                    //since we've captured all varargs already, it does not make sense to process other matchers.
                    return;
                } else {
                    ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[position]);
                }
            }
        }
    }
}