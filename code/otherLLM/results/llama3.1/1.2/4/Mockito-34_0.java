public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    for (CapturesArguments m : getMatchers()) {
        if (k < i.getArguments().length) { // Handle edge case where number of matchers exceeds number of arguments
            ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
            k++;
        }
    }
}