public void captureArgumentsFrom(Invocation invocation) {
    if (invocation.getMethod().isVarArgs()) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        
        for (int position = 0; position <= matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                Object array = invocation.getRawArguments()[position];
                
                if(array == null){
                    ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[indexOfVararg]);
                } else {
                    for (int i = 0; i < Array.getLength(array); i++) {
                        ((CapturesArguments) m).captureFrom(Array.get(array, i));
                    }
                    
                    //since we've captured all varargs already, it does not make sense to process other matchers.
                    return;
                }
            }
        }
        
    } else {
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }
    }
}