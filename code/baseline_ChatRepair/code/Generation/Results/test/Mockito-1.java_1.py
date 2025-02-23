public void captureArgumentsFrom(Invocation invocation) {
    Object[] rawArguments = invocation.getRawArguments();
    int argumentsLength = rawArguments.length;
    
    for (int i = 0; i < matchers.size(); i++) {
        Matcher matcher = matchers.get(i);
        if (matcher instanceof CapturesArguments) {
            CapturesArguments captures = (CapturesArguments) matcher;

            if (invocation.getMethod().isVarArgs() && i >= argumentsLength - 1) {
                // Handle varargs
                Object varArgArray = rawArguments[argumentsLength - 1];
                if (varArgArray != null && varArgArray.getClass().isArray()) {
                    int arrayLength = Array.getLength(varArgArray);
                    for (int j = 0; j < arrayLength; j++) {
                        captures.captureFrom(Array.get(varArgArray, j));
                    }
                } else {
                    // Handle the case where the vararg array itself might be null or not an array
                    captures.captureFrom(varArgArray);
                }
                // Only break if we are matching specifically the varargs part and it's the last matcher
                if (i == matchers.size() - 1) {
                    break; 
                }
            } else {
                // Handle regular arguments
                if (i < argumentsLength) {
                    captures.captureFrom(rawArguments[i]);
                } else {
                    captures.captureFrom(null);  // Capture null if there are more matchers than arguments
                }
            }
        }
    }
}
