public long adjustOffset(long instant, boolean earlierOrLater) {
    // Evaluate 3 hours before and after to determine if there's an overlap or gap
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);

    // If the local time before and after are the same, there's no overlap or gap
    if (instantBefore == instantAfter) {
        return instant;  // not an overlap or gap (equal is normal case)
    }

    // Determine the local time for the given instant
    long local = convertUTCToLocal(instant);

    // During an overlap, local times can map to two different UTC times
    if (instantBefore != instantAfter) {
        // Determine the offset transitions to handle the overlap correctly
        boolean transition = (instantBefore > instantAfter);

        // Use earlierOrLater to decide which UTC time to pick during the overlap
        if (earlierOrLater) {
            // User prefers the later UTC time during overlap if transition from standard to DST
            return convertLocalToUTC(local, false, transition ? instantAfter : instantBefore);
        } else {
            // User prefers the earlier UTC time during overlap if transition from DST to standard
            return convertLocalToUTC(local, false, transition ? instantBefore : instantAfter);
        }
    }

    // Default return if no conditions are met (should not reach here in valid cases)
    return instant;
}