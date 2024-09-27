public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    for (Matcher m : matchers) {
        // Ensure k is within the bounds of the arguments array
        if (k < i.getArguments().length) {
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
            }
        }
        k++;
    }
}