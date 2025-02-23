public void captureArgumentsFrom(Invocation invocation) {
    Object[] rawArguments = invocation.getRawArguments();
    boolean isVarArgs = invocation.getMethod().isVarArgs();
    int numberOfArgs = rawArguments.length;
    int fixedArgumentsCount = isVarArgs ? numberOfArgs - 1 : numberOfArgs;

    for (int position = 0; position < matchers.size(); position++) {
        Matcher m = matchers.get(position);
        if (m instanceof CapturesArguments) {
            CapturesArguments captures = (CapturesArguments) m;
            
            if (isVarArgs && position >= fixedArgumentsCount) {
                // Process varargs
                Object array = rawArguments[fixedArgumentsCount];
                if (array != null && array.getClass().isArray()) {
                    int arrayLength = Array.getLength(array);
                    for (int i = 0; i < arrayLength; i++) {
                        captures.captureFrom(Array.get(array, i));
                    }
                } else {
                    // In case there's only one vararg element which is not an array or is null.
                    captures.captureFrom(array);
                }
                // Since subsequent matchers will try to match against the same varargs array, break if required.
                break;
            } else {
                // Regular argument processing
                if (position < numberOfArgs) {
                    captures.captureFrom(rawArguments[position]);
                } else {
                    captures.captureFrom(null); // Capture with null if there are fewer actual arguments than matchers.
                }
            }
        }
    }
}
