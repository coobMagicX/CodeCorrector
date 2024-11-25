public void captureArgumentsFrom(Invocation i) {
    int capturedCount = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            ((CapturesArguments) m).captureFrom(i.getArguments()[capturedCount]);
            capturedCount++;
        }
    }

    // Validate if the captured argument count matches the actual argument count
    if (capturedCount != i.getArguments().length) {
        throw new RuntimeException("Number of captured arguments does not match the actual number of arguments.");
    }
}