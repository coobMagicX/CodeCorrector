public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    for (Matcher m : getMatchers()) { // Use the method to retrieve matchers instead of hardcoding them.
        if (m instanceof CapturesArguments) {
            if (k < i.getArguments().length) { // Check if k is within the bounds of i.getArguments() length
                ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
                k++; // Increment k only after a successful capture
            } else {
                break; // If k is out of bounds, exit the loop
            }
        }
    }
}