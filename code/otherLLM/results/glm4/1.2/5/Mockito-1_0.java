public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        // Assuming we need to handle varargs properly, let's remove the throw statement and process varargs accordingly.
        
        // Capture arguments up to the point before varargs start
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }

        // Now, handle the varargs
        for (int i = indexOfVararg; i < invocation.getRawArguments().length; i++) {
            Matcher m = matchers.get(i);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[i]);
            }
        }

    } else {
        // Handle non-varargs method
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }
    }

    // The additional for-loop and logic to check for varargs has been integrated into the correct sections of the method
}