public long adjustOffset(long instant, boolean earlierOrLater) {
    // a bit messy, but will work in all non-pathological cases
    
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    
    // Check for overlap or gap between two different offsets
    if (instantBefore == instantAfter) {
        return instant;  // not an overlap (less than is a gap, equal is normal case)
    }
    
    // work out range of instants that have duplicate local times
    long local = convertUTCToLocal(instant);
    
    // calculate result based on the desired offset direction
    int desiredOffset;
    if (earlierOrLater) {
        desiredOffset = getOffsetFromLocal(local);
    } else {
        desiredOffset = getOffsetFromLocal(local);
    }
    
    return convertLocalToUTC(local, false, desiredOffset);
}