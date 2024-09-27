public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        Object array = invocation.getRawArguments()[indexOfVararg];
        for (int i = 0; i < Array.getLength(array); i++) {
            for (int position = 0; position < matchers.size(); position++) {
                Matcher m = matchers.get(position);
                if (m instanceof CapturesArguments && invocation.getRawArguments().length > position) {
                    if (isVarargMatcher(m)) {
                        ((CapturesArguments) m).captureFrom(Array.get(array, i));
                    } else {
                        ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[position]);
                    }
                }
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