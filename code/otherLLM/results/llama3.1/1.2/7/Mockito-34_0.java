public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
        }
        // Removed the increment of k outside the loop condition
    }
}