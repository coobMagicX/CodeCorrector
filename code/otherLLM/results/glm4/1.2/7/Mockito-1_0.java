public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        for (int position = 0; position <= indexOfVararg; position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments && isVariableArgument(invocation, position) && isVarargMatcher(m)) {
                Object array = invocation.getRawArguments()[position];
                if (array != null) { // Check for a non-null array before iterating
                    for (int i = 0; i < Array.getLength(array); i++) {
                        ((CapturesArguments) m).captureFrom(Array.get(array, i));
                    }
                }
            } else if (m instanceof CapturesArguments && position == indexOfVararg) {
                // Handle the varargs separately outside of the loop to avoid duplicate processing
                Object[] varargs = Arrays.copyOfRange((Object[]) invocation.getRawArguments()[indexOfVararg], 0, Array.getLength(invocation.getRawArguments()[indexOfVararg]));
                for (int i = 0; i < varargs.length; i++) {
                    ((CapturesArguments) m).captureFrom(varargs[i]);
                }
            }
        }

    } else {
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments && invocation.getRawArguments().length > position) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }
    }
}