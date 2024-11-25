public long adjustOffset(long instant, boolean earlierOrLater) {
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);

    // Check for non-overlapping cases by comparing instants
    if (instantBefore == instantAfter) {
        return instant; // not an overlap (less than is a gap, equal is normal case)
    }

    // Find the local time at the given instant
    long local = convertUTCToLocal(instant);

    int offset = getOffsetFromLocal(local);
    
    // Determine if we need to adjust for earlier or later offset based on overlap
    boolean isOverlap;
    if (earlierOrLater) {
        isOverlap = (convertLocalToUTC(local, false, local) == instantAfter);
    } else {
        isOverlap = (convertLocalToUTC(local, false, local) == instantBefore);
    }
    
    // Adjust the offset based on whether it's an overlap or not
    if (isOverlap) {
        return convertUTCToLocal(instant); // Return the same instant in case of an overlap
    } else {
        long nextTransition = nextTransition(local);
        int nextOffset = getOffset(nextTransition);
        
        // Adjust to the new offset after DST transition
        if ((offset < 0 && earlierOrLater) || (offset > 0 && !earlierOrLater)) {
            return convertLocalToUTC(nextTransition, false, nextTransition);
        } else {
            return nextTransition;
        }
    }
}