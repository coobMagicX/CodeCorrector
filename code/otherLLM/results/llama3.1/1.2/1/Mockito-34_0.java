public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
        }
        k++;
        
        // Check to prevent ArrayIndexOutOfBoundsException
        if (k >= i.getArguments().length) {
            throw new IllegalArgumentException("Number of capture matches exceeds actual argument count");
        }
    }
}