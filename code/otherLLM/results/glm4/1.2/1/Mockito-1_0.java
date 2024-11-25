public void captureArgumentsFrom(Invocation invocation) {
    // Check if the method is using varargs
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        
        // Process all arguments except for the last one as varargs
        for (int position = 0; position < indexOfVararg; position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[position]);
            }
        }

        // Process the varargs
        Object[] varargs = (Object[]) invocation.getRawArguments()[indexOfVararg];
        for (int i = 0; i < varargs.length; i++) {
            Matcher m = matchers.get(indexOfVararg);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(varargs[i]);
            }
        }
    } else {
        // Process regular arguments
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[position]);
            }
        }
    }
}