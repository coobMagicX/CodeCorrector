public long adjustOffset(long instant, boolean earlierOrLater) {
    // a bit messy, but will work in all non-pathological cases
    
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    
    // Check for overlap or gap by comparing the before and after instants
    if (instantBefore == instantAfter) {
        return instant;  // not an overlap (less than is a gap, equal is normal case)
    }
    
    // Work out the local time from UTC
    long local = convertUTCToLocal(instant);
    
    // Determine whether to go to the later or earlier offset based on the 'earlierOrLater' flag
    boolean useAfterInstant = earlierOrLater;
    if (isLocalDateTimeGap(new LocalDateTime(local))) {
        // If there is a gap, we need to choose the offset before the transition instead of after
        useAfterInstant = !useAfterInstant;
    }
    
    return convertLocalToUTC(local, false, useAfterInstant ? instantAfter : instantBefore);
}

// Helper method to determine if the local date-time has a gap (e.g., during DST transition)
private boolean isGap(long instant) {
    LocalDateTime localDateTime = new LocalDateTime(instant);
    return isLocalDateTimeGap(localDateTime);
}