public void captureArgumentsFrom(Invocation i) {
    Object[] arguments = i.getArguments();
    int limit = Math.min(arguments.length, matchers.size());
    for (int k = 0; k < limit; k++) {
        Matcher m