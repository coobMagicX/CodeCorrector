public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    for (int j = 0; j < matchers.size(); j++) { // Loop through the matchers list instead of iterating over its elements directly
        Matcher m = matchers.get(j); // Get each matcher instance individually
        if (m instanceof CapturesArguments) {
            ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
            k++; // Increment index after capturing arguments
        } else if (j == 0) { // If the current matcher does not match, break out of the loop
            break;
        }
    }
}