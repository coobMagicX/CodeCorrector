public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            if (k < i.getArguments().length) {
                ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
            }
            // Do not increment k if it is already at the length of i.getArguments()
            else {
                break;
            }
        }
    }
}