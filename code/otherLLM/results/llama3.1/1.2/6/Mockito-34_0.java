public void captureArgumentsFrom(Invocation i) {
    int expectedArgs = 0, actualArgs = i.getArguments().length;
    for (CapturesArguments m : getMatchers()) {
        if (expectedArgs < actualArgs) {
            m.captureFrom(i.getArguments()[expectedArgs]);
            expectedArgs++;
        } else if (expectedArgs > actualArgs) {
            throw new IllegalArgumentException("Expected " + expectedArgs + " arguments, but got " + actualArgs);
        }
    }
}