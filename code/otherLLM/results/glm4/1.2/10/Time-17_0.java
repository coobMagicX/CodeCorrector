public long adjustOffset(long instant, boolean earlierOrLater) {
    // Evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    
    // Check if the time before and after are equal, which means no DST overlap
    if (instantBefore == instantAfter) {
        return instant;  // not an overlap (less than is a gap, equal is normal case)
    }
    
    // Work out the range of instants that have duplicate local times
    long local = convertUTCToLocal(instant);
    
    // Check if we are in the DST gap or overlap period
    int offset = getOffsetFromLocal(local);
    
    // If earlierOrLater is true, we want to go towards the 'after' instant; otherwise, towards the 'before' instant
    long targetInstant;
    if (earlierOrLater) {
        targetInstant = instantAfter;
    } else {
        targetInstant = instantBefore;
    }
    
    // Convert local time back to UTC with the adjusted offset
    return convertLocalToUTC(local, false, targetInstant);
}