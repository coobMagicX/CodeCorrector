public void captureArgumentsFrom(Invocation i) {
    Object[] arguments = i.getArguments();
    int numArguments = arguments.length;

    int k = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            if (k < numArguments) {
                ((CapturesArguments) m).captureFrom(arguments[k]);
           