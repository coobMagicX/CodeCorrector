public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        Object[] varargsArray = invocation.getRawArguments();
        for (int position = 0; position <= indexOfVararg; position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(varargsArray[position]);
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

    // Rest of the code remains the same
    for (int position = 0; position < matchers.size(); position++) {
        Matcher m = matchers.get(position);
        if (m instanceof CapturesArguments && invocation.getRawArguments().length > position) {
            //TODO SF - this whole lot can be moved captureFrom implementation
            if(isVariableArgument(invocation, position) && isVarargMatcher(m)) {
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