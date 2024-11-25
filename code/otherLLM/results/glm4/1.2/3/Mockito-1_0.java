public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        
        // Check the number of arguments before proceeding with varargs handling
        if (indexOfVararg + 1 != matchers.size()) {
            throw new IllegalArgumentException("The number of matchers does not match the expected count for varargs.");
        }
        
        Matcher m = matchers.get(indexOfVararg);
        if (!isVarargMatcher(m)) {
            throw new IllegalArgumentException("The last matcher is not a vararg matcher as expected.");
        }
        
        Object array = invocation.getRawArguments()[indexOfVararg];
        for (int i = 0; i < Array.getLength(array); i++) {
            ((CapturesArguments) m).captureFrom(Array.get(array, i));
        }
    } else {
        // Check the number of arguments before proceeding with non-varargs handling
        if (invocation.getRawArguments().length != matchers.size()) {
            throw new IllegalArgumentException("The number of arguments does not match the expected count.");
        }

        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }
    }
}