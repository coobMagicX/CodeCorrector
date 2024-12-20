public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        
        for (int position = indexOfVararg; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
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

    int remainingArgs = invocation.getRawArguments().length - indexOfVararg;
    for (int position = 0; position < matchers.size(); position++) {
        Matcher m = matchers.get(position);
        if (m instanceof CapturesArguments && remainingArgs > 0) {
            if(isVariableArgument(invocation, indexOfVararg + position) && isVarargMatcher(m)) {
                Object array = invocation.getRawArguments()[indexOfVararg + position];
                for (int i = 0; i < Array.getLength(array); i++) {
                    ((CapturesArguments) m).captureFrom(Array.get(array, i));
                }
                remainingArgs--;
            } else {
                ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[indexOfVararg + position]);
                remainingArgs--;
            }
        }
    }
}