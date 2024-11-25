public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            while (k < i.getArguments().length && k < matchers.size()) {
                ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
                k++;
            }
            // If the loop exits early due to a mismatch in argument count, break out of the loop
            if (k >= i.getArguments().length || k >= matchers.size()) {
                break;
            }
        }
    }
}