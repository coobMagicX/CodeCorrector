public void captureArgumentsFrom(Invocation invocation) {
    int argumentsLength = invocation.getRawArguments().length;
    boolean isVarArgs = invocation.getMethod().isVarArgs();
    int lastIndex = matchers.size() - 1;
    
    for (int position = 0; position < matchers.size(); position++) {
        Matcher m = matchers.get(position);
        
        if (m instanceof CapturesArguments) {
            if (isVarArgs && position == lastIndex) {
                // Handle the vararg argument
                Object array = invocation.getRawArguments()[position];
                int arrayLength = Array.getLength(array);
                for (int i = 0; i < arrayLength; i++) {
                    ((CapturesArguments) m).captureFrom(Array.get(array, i));
                }
            } else {
                // Handle regular argument
                if (position < argumentsLength) { // Safety check for index
                    ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[position]);
                }
            }
        }
    }
}
