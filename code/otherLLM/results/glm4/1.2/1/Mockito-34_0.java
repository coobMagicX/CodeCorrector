public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            // Check that k is within the bounds of i.getArguments()
            if (k < i.getArguments().length) {
                ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
            } else {
                // Handle the case where k is out of bounds
                break;
            }
        }
        k++;
    }
}