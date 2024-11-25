public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            if (safelyArgumentsMatch(i.getArguments()[k])) {
                ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
            } else {
                // If the captured argument count does not match, try to capture from a different index
                for (int j = 0; j < i.getArguments().length; j++) {
                    if (safelyArgumentsMatch(i.getArguments()[j])) {
                        ((CapturesArguments) m).captureFrom(i.getArguments()[j]);
                        break;
                    }
                }
            }
        }
        k++;
    }
}