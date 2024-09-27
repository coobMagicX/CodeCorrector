public void captureArgumentsFrom(Invocation i) {
    // No need to throw an exception if the number of matchers does not match the number of arguments
    // Iterate using the smaller of the two counts to prevent ArrayIndexOutOfBoundsException
    int count = Math.min(i.getArguments().length, matchers.size());

    for (int k = 0; k < count; k++) {
        Matcher m = matchers.get(k);
        if (m instanceof CapturesArguments) {
            ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
        }
    }
}