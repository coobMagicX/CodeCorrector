public void captureArgumentsFrom(Invocation i) {
    // Initialize an index to keep track of arguments
    int argumentIndex = 0;
    
    // Iterate over matchers
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) {
            // Ensure there are enough arguments in the invocation
            if (argumentIndex < i.getArguments().length) {
                ((CapturesArguments) m).captureFrom(i.getArguments()[argumentIndex]);
                argumentIndex++; // Increment the index for the next matcher
            } else {
                // Handle the case where there are no more arguments to capture
                // This could be logging an error, throwing an exception, or skipping this matcher
                // For example, let's log a warning and break out of the loop
                System.out.println("Warning: No more arguments to capture for matchers after index " + argumentIndex);
                break;
            }
        }
    }
}