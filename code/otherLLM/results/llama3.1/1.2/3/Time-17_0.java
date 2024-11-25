public long adjustOffset(long instant, boolean earlierOrLater) {
    // a bit messy, but will work in all non-pathological cases
    
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    
    // Check for edge case where two instants have the same local time but different UTC times
    if (instantBefore == instantAfter) {
        return earlierOrLater ? instantAfter : instantBefore;  // adjust offset based on whether it's earlier or later
    }
    
    long local = convertUTCToLocal(instant);
    return convertLocalToUTC(local, false, earlierOrLater ? instantAfter : instantBefore);
}