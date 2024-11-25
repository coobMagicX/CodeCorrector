public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        for (int position = indexOfVararg; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                Object array = invocation.getRawArguments()[position];
                for (int i = 0; i < Array.getLength(array); i++) {
                    ((CapturesArguments) m).captureFrom(Array.get(array, i));
                }
            }
        }
    } else {
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments && invocation.getRawArguments().length > position) {
                ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[position]);
            }
        }
    }
}