public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        Object varargArray = invocation.getRawArguments()[indexOfVararg];
        for (int i = 0; i < Array.getLength(varargArray); i++) {
            for (int position = 0; position < matchers.size(); position++) {
                Matcher m = matchers.get(position);
                if (m instanceof CapturesArguments && position < invocation.getRawArguments().length) {
                    ((CapturesArguments) m).captureFrom(Array.get(varargArray, i));
                }
            }
        }
    } else {
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments && position < invocation.getRawArguments().length) {
                ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[position]);
            }
        }
    }
}