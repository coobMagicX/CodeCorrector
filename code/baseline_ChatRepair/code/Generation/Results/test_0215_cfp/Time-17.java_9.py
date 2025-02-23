public long adjustOffset(long instant, boolean earlierOrLater) {
    // Evaluate three hours before and after the specific instant to determine if there is an overlap
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);

    // If these are equal, there is no overlap or gap - so return the instant directly
    if (instantBefore == instantAfter) {
        return instant;  // Normal case, neither overlap nor gap
    }
    
    // There is an overlap or gap. Determine the correct UTC instant at the local time depending on 'earlierOrLater'.
    long local = convertUTCToLocal(instant);
    if (earlierOrLater) {
        // Want the later offset during overlap
        // Find where instant + 3 hours local time is the same (duplicate), adjust instant to this new offset
        long newInstant = convertLocalToUTC(local, false, instantAfter);
        return newInstant;
    } else {
        // Want the earlier offset during overlap
        // Find where instant - 3 hours local time was the same (duplicate), adjust instant to this older offset
        long newInstant = convertLocalToUTC(local, false, instantBefore);
        return newInstant;
    }
}
