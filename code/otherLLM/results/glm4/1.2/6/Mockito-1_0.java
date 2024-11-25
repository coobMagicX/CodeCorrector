public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        throw new UnsupportedOperationException("Varargs are not supported");
    }

    // Handle non-varargs arguments
    for (int position = 0; position < matchers.size(); position++) {
        Matcher m = matchers.get(position);
        if (m instanceof CapturesArguments) {
            ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
        }
    }

    // Handle varargs explicitly after processing non-vararg arguments
    for (int position = 0; position < matchers.size(); position++) {
        Matcher m = matchers.get(position);
        if (m instanceof CapturesArguments) {
            int argIndex = invocation.getRawArguments().length - 1;
            if (position == argIndex) { // Check if the current matcher corresponds to vararg position
                Object array = invocation.getRawArguments()[argIndex];
                for (int i = 0; i < Array.getLength(array); i++) {
                    ((CapturesArguments) m).captureFrom(Array.get(array, i));
                }
            }
        }
    }
}