for (int position = 0; position < matchers.size(); position++) {
    Matcher m = matchers.get(position);
    if (m instanceof CapturesArguments && invocation.getRawArguments().length > position) {
        if (isVariableArgument(invocation, position) && isVarargMatcher(m)) {
            Object array = invocation.getRawArguments()[position];
            if (array != null) {
                for (int i = 0; i < Array.getLength(array); i++) {
                    ((CapturesArguments) m).captureFrom(Array.get(array, i));
                }
            }
            return;
        } else {
            ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[position]);
        }
    }
}