public void captureArgumentsFrom(Invocation i) {
    Object[] arguments = i.getArguments(); 
    int k = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            if (k >= arguments.length) {
                