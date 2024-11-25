public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            try {
                ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
            } catch (ArrayIndexOutOfBoundsException e) {
                // Handle out-of-bounds indexing by skipping this matcher
            }
        }
        k++;
    }
}