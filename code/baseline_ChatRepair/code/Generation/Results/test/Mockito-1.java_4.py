public void captureArgumentsFrom(Invocation invocation) {
    Object[] rawArguments = invocation.getRawArguments();

    for (int position = 0; position < matchers.size(); position++) {
        Matcher m = matchers.get(position);

        if (m instanceof CapturesArguments) {
            if (invocation.getMethod().isVarArgs() && position == rawArguments.length - 1) {
                // This position holds a vararg array
                Object array = rawArguments[position];
                int arrayLength = Array.getLength(array);
                if (arrayLength > 0) {
                    for (int i = 0; i < arrayLength; i++) {
                        ((CapturesArguments) m).captureFrom(Array.get(array, i));
                    }
                } else {
                    // The varargs are empty so directly pass the empty array
                    ((CapturesArguments) m).captureFrom(array);
                }
                // Exit the loop after handling varargs, assuming only the last matcher is meant for varargs
                break;
            } else {
                // Normal argument processing
                if (position < rawArguments.length) {
                    ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
                }
            }
        }
    }
}
