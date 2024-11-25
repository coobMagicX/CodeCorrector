public void captureArgumentsFrom(Invocation i) {
    int expectedCount = 0; // Placeholder for the expected count of arguments
    int actualCount = i.getArguments().length;
    
    if (actualCount != expectedCount) {
        throw new IllegalArgumentException("Incorrect number of arguments provided.");
    }
    
    int k = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
            k++;
            // Ensure that we do not exceed the actual count of arguments
            if (k >= actualCount) break;
        }
    }
}