public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        Object varargArray = invocation.getRawArguments()[indexOfVararg];
        for (int i = 0; i < Array.getLength(varargArray); i++) {
            Object varargElement = Array.get(varargArray, i);
            for (int position = 0; position < matchers.size(); position++) {
                Matcher m = matchers.get(position);
                if (m instanceof CapturesArguments) {
                    if (position == indexOfVararg && isVarargMatcher(m)) {
                        ((CapturesArguments) m).captureFrom(varargElement);
                    } else if (position < indexOfVararg) {
                        ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
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