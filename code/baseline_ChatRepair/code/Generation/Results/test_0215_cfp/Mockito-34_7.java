public void captureArgumentsFrom(Invocation i) {
    Object[] arguments = i.getArguments();
    int numArguments = arguments.length;
    int k = 0;
    for (Matcher m : matchers) {
        
        if (k >= num