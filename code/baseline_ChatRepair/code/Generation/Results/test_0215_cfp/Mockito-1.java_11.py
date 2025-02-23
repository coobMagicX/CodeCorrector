public void captureArgumentsFrom(Invocation invocation) {
    // Iterate over all the matchers.
    for (int position = 0; position < matchers.size(); position++) {
        Matcher m = matchers.get(position);
        // Check first if the matcher can capture the arguments.
        if (m instanceof CapturesArguments) {
            // Standard Argument Capture: Check bounds to avoid ArrayIndexOutOfBoundsException.
            if (position < invocation.getRawArguments().length - 1 || !invocation.getMethod().isVarArgs()) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
            // Variadic Argument Capture: Process varargs which means the last argument.
            else if (invocation.getMethod().isVarArgs() && position == invocation.getRawArguments().length - 1) {
                Object array = invocation.getRawArguments()[position];
                for (int i = 0; i < Array.getLength(array); i++) {
                    ((CapturesArguments) m).captureFrom(Array.get(array, i));
                }
            }
        }
    }
}
