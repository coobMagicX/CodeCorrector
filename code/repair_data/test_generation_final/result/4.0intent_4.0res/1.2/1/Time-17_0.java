public long adjustOffset(long instant, boolean earlierOrLater) {
    // Evaluate 3 hours before and after to determine if an overlap or gap exists
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);

    if (instantBefore == instantAfter) {
        return instant;  // No overlap or gap, normal case
    }

    // Determine the range of UTC instants that map to the same local instant
    long local = convertUTCToLocal(instant);
    
    // Determine if the local instant is during the overlap
    if (earlierOrLater) {
        return convertLocalToUTC(local, false, instant + 1 * DateTimeConstants.MILLIS_PER_HOUR);
    } else {
        return convertLocalToUTC(local, false, instant - 1 * DateTimeConstants.MILLIS_PER_HOUR);
    }
}