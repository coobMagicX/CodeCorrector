public void captureArgumentsFrom(Invocation i) {
    List<Matcher> matchers = getMatchers(); // Get the list of matchers from the context
    int expectedCount = 0;
    
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            // Check to see if k is within bounds of i.getArguments().length
            if (k < i.getArguments().length) {
                ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
                k++;
            } else {
                // Handle the case where there are more matchers than arguments
                break; // Exit the loop since there are no more arguments to capture
            }
        }
    }
}