public void captureArgumentsFrom(Invocation i) {
    Object[] arguments = i.getArguments();
    for (int k = 0; k < arguments.length; k++) {
        Matcher m = matchers.get(k);
        if (m instanceof CapturesArguments) {
            ((CapturesArguments) m).captureFrom(arguments[k]);
        }
    }
}
