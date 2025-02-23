public long adjustOffset(long instant, boolean earlierOrLater) {
    // Evaluate 3 hours before and after to work out if anything is happening around the instant
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);

    // If nothing changes between before and after, there is no overlap or gap
    if (instantBefore == instantAfter) {
        return instant; // No adjustment needed, just return the original instant
    }

    // Find the exact bounds of where the local time doesn't match the expected UTC times
    long overlapLowerBound = instant;
    long newInstant = instant;
    long step = DateTimeConstants.MILLIS_PER_HOUR;
    
    // Search backward to find the lower bound of the overlap
    while (convertUTCToLocal(overlapLowerBound - step) != instantBefore) {
        overlapLowerBound -= step;
    }

    // Search forward to refine the lower bound to the exact point of overlap
    while (convertUTCToLocal(overlapLowerBound) == instantBefore) {
        overlapLowerBound += DateTimeConstants.MILLIS_PER_MINUTE;
    }

    // Determine the adjustment based on the earlierOrLater flag
    if (earlierOrLater) {
        // Select the later offset (the one after the DST jump)
        long offsetPoint = overlapLowerBound - 1; // Pick just before the change
        newInstant = convertLocalToUTC(convertUTCToLocal(offsetPoint), earlierOrLater, offsetPoint);
    } else {
        // Select the earlier offset (the one before the DST jump)
        newInstant = convertLocalToUTC(convertUTCToLocal(overlapLowerBound), earlierOrLater, overlapLowerBound);
    }

    return newInstant;
}
