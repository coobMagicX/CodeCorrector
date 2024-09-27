public long adjustOffset(long instant, boolean earlierOrLater) {
    // Evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    
    // If the local times before and after are the same, there is no time zone change event
    if (instantBefore == instantAfter) {
        return instant;  // No overlap or gap, return the original instant
    }
    
    // Calculate the local representation of the instant
    long local = convertUTCToLocal(instant);
    
    // Adjust the instant based on the earlierOrLater flag and whether there's an overlap or gap
    if (instantBefore != instantAfter) {
        // Determine the correct millisecond instant that reflects the desired overlap or gap handling
        long newInstant = earlierOrLater ? instantAfter : instantBefore;
        return convertLocalToUTC(local, false, newInstant);
    } else {
        // No adjustment needed, return the original instant
        return instant;
    }
}