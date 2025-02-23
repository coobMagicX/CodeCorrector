public void captureArgumentsFrom(Invocation invocation) {
    boolean isVarArgs = invocation.getMethod().isVarArgs();
    Object[] rawArguments = invocation.getRawArguments();
    int indexBeforeVarArgs = matchers.size() - 1;
    
    for (int position = 0; position < matchers.size(); position++) {
        Matcher m = matchers.get(position);
        
        // If Matcher supports argument capture, handle capturing:
        if (m instanceof CapturesArguments) {
            if (isVarArgs && position == indexBeforeVarArgs) {
                // Handle the varargs section
                captureVarArgs((CapturesArguments) m, rawArguments[position]);
            } else {
                // Handle regular arguments; still check if in bounds
                if (position < rawArguments.length) {
                    ((CapturesArguments) m).captureFrom(rawArguments[position]);
                }
            }
        }
    }
}

private void captureVarArgs(CapturesArguments matcher, Object varArray) {
    int arrayLength = Array.getLength(varArray);
    for (int i = 0; i < arrayLength; i++) {
        matcher.captureFrom(Array.get(varArray, i));
    }
}
