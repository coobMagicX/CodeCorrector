public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            if (i.getArguments().length > k) {
                ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
            } else {
                // Handle the case where there are no more arguments to capture
                break;
            }
        }
    }
}