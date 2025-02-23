public void captureArgumentsFrom(Invocation invocation) {
    for (int position = 0; position < matchers.size(); position++) {
        Matcher m = matchers.get(position);
        if (m instanceof CapturesArguments) {
            // Check if we are accessing a varargs part in the argument list
            if (invocation.getMethod().isVarArgs() && position == invocation.getRawArguments().length - 1) {
                Object array = invocation.getRawArguments()[position];
                int arrayLength = Array.getLength(array);
                for (int i = 0; i < arrayLength; i++) {
                    ((CapturesArguments) m).captureFrom(Array.get(array, i));
                }
                // All vararg arguments have been captured; no need to process further elements in matchers
                break;
            } else {
                if (position < invocation.getRawArguments().length) {
                    ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
                }
            }
        }
    }
}
