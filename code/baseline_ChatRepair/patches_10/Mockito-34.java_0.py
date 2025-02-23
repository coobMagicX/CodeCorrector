public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    Object[] arguments = i.getArguments();
    for (Matcher m : matchers) {
        if (k < arguments.length && m instanceof CapturesArguments) {
            ((CapturesArguments) m).captureFrom(arguments[k]);
        }
        k++;
    }
}
