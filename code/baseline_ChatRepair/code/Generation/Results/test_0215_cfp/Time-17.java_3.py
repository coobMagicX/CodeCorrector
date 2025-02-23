public long adjustOffset(long instant, boolean earlierOrLater) {
    // evaluate 3 hours before and after to determine if there is a transition
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);

    // When instantBefore equals instantAfter, there is no transition
    if (instantBefore == instantAfter) {
        return instant; // this is a normal case or a gap, return the original instant
    }

    // Calculate the local timestamp for the given instant
    long localInstant = convertUTCToLocal(instant);
    
    // Determine the offset window within which the transition occurs
    if (earlierOrLater) {
        // Prefer the later offset
        if (localInstant == instantBefore) {
            // The original local instant corresponds to the earlier offset instant
            return convertLocalToUTC(localInstant, false, instant + 1);
        } else {
            // Otherwise, use the later offset normally
            return convertLocalToUTC(localInstant, false, instant);
        }
    } else {
        // Prefer the earlier offset
        long newInstant = convertLocalToUTC(localInstant, true, instant);
        if (convertUTCToLocal(newInstant) != localInstant) {
            // Adjust to previous valid offset if standard conversion doesn't match
            newInstant = convertLocalToUTC(localInstant - DateTimeConstants.MILLIS_PER_HOUR, false, instant);
        }
        return newInstant;
    }
}
