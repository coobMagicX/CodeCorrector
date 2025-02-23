public void captureArgumentsFrom(Invocation invocation) {
    Object[] rawArguments = invocation.getRawArguments();
    int rawArgLength = rawArguments.length;
    
    // Handle regular and variable arguments separately.
    for (int position = 0; position < matchers.size(); position++) {
        Matcher m = matchers.get(position);
        
        if (!(m instanceof CapturesArguments)) {
            continue;
        }
        
        // Check if we're at the vararg position
        if (invocation.getMethod().isVarArgs() && position == matchers.size() - 1) {
            // Handle variable arguments - it's expected to be the last parameter and an array
            Object varArgArray = rawArguments[position];
            if (varArgArray != null) {
                int arrayLength = Array.getLength(varArgArray);
                for (int i = 0; i < arrayLength; i++) {
                    ((CapturesArguments) m).captureFrom(Array.get(varArgArray, i));
                }
            }
        } else {
            if (position < rawArgLength) {
                // Process as regular argument
                ((CapturesArguments) m).captureFrom(rawArguments[position]);
            }
        }
    }
}
