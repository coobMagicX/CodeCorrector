public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    Object[] arguments = i.getArguments();
    if (arguments.length != matchers.length) {
        throw new IllegalArgumentException("Number of arguments in the invocation does not match number of matchers.");
    }
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            ((CapturesArguments) m).captureFrom(arguments[k]);
        }
        k++;
    }
}