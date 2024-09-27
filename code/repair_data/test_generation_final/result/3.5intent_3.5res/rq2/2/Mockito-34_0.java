public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    Object[] arguments = i.getArguments();
    for (int j = 0; j < matchers.length && k < arguments.length; j++) {
        Matcher m = matchers[j];
        if (m instanceof CapturesArguments) {
            ((CapturesArguments) m).captureFrom(arguments[k]);
        }
        k++;
    }
}