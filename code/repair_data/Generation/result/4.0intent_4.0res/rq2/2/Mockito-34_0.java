public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            if (k < i.getArguments().length) { // Ensure the index is within the bounds of the array
                ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
            }
        }
        k++;
    }
}