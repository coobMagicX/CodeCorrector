public long adjustOffset(long instant, boolean earlierOrLater) {
    // a bit messy, but will work in all non-pathological cases
    
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    
    // Determine if there is an overlap by comparing times before and after the instant
    if (instantBefore == instantAfter) {
        // No overlap detected
        return instant;  // not an overlap (less than is a gap, equal is normal case)
    }
    
    // Calculate the local time at the given instant
    long local = convertUTCToLocal(instant);
    
    // If there is overlap, determine the correct offset to use based on earlierOrLater flag
    // Fix: Ensure the correct parameter usage for the convertLocalToUTC method during overlap
    // Assume that instantBefore and instantAfter now correctly represent the range of possible valid UTC times for the overlapping local time
    return convertLocalToUTC(local, !earlierOrLater, earlierOrLater ? instantAfter : instantBefore);
}