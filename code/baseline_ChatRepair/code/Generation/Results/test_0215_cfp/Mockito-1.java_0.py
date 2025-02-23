public void captureArgumentsFrom(Invocation invocation) {
    for (int position = 0; position < matchers.size(); position++) {
        Matcher m = matchers.get(position);
        if (m instanceof CapturesArguments) {
            if (invocation.getMethod().isVarArgs() && position == invocation.getRawArguments().length - 1) {
                Object array = invocation.getArgumentAt(position, Object.class);
                if (array.getClass().isArray()) {
                    for (int i = 0; i < Array.getLength(array); i++) {
                        ((CapturesArguments) m).captureFrom(Array.get(array, i));
                    }
                } else {
                    // in case the vararg array itself is null, capture it directly
                    ((CapturesArguments) m).captureFrom(array);
                }
                break; // exit after processing varargs
            } else {
                Object argument = invocation.getRawArguments().length > position ? 
                    invocation.getArgumentAt(position, Object.class) : null;
                ((CapturesArguments) m).captureFrom(argument);
            }
        }
    }
}
