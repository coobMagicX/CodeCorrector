public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments && k < i.getArguments().length) {
            ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
        }
        k++;
    }
}