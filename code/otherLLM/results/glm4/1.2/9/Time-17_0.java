public long adjustOffset(long instant, boolean earlierOrLater) {
    // a bit messy, but will work in all non-pathological cases
    
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    
    // Check if we are near a DST boundary
    int offsetBefore = getOffset(instantBefore);
    int offsetAfter = getOffset(instantAfter);
    boolean isOverlap = offsetBefore != offsetAfter;
    
    if (isOverlap) {
        // We are in an overlap situation. Calculate the next and previous transition times.
        long nextLocal = nextTransition(instantBefore);
        long prevLocal = previousTransition(instantBefore);
        
        // If instant falls into a DST gap, adjust accordingly
        if ((nextLocal == (instantBefore - offsetBefore)) && (prevLocal != nextLocal)) {
            return convertUTCToLocal(nextLocal);
        } else if ((prevLocal == (instantBefore - offsetBefore)) && (nextLocal != prevLocal)) {
            return convertUTCToLocal(prevLocal);
        }
    }
    
    // If not an overlap or we have adjusted for DST gap, proceed to adjust the offset
    long local = convertUTCToLocal(instant);
    long targetInstant = earlierOrLater ? instantBefore : instantAfter;
    return convertLocalToUTC(local, false, targetInstant == instantAfter);
}