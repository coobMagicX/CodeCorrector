public void captureArgumentsFrom(Invocation i) {
    int expectedCount = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            expectedCount++; // Increase count of expected arguments to be captured
        }
    }

    int actualCount = i.getArguments().length;
    if (expectedCount != actualCount) {
        throw new IllegalArgumentException("The number of arguments does not match the expected count: Expected " + expectedCount + ", but got " + actualCount);
    }

    int k = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
            k++;
        }
    }
}