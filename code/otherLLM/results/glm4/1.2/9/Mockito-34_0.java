public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            if (k < i.getArguments().length) {
                ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
                k++;
            } else {
                // Handle the case where there are no more arguments to capture from
                // This could be by breaking out of the loop, logging an error, etc.
                break;
            }
        }
    }
}