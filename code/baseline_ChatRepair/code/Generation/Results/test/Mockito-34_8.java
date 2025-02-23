public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    Object[] arguments = i.getArguments();
    for (Matcher m : matchers) {
