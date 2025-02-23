public long adjustOffset(long instant, boolean earlierOrLater) {
    // Calculate the instant for three hours before and after the given instant
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    
    // If there's no change in the offset, return the instant itself
    if (instantBefore == instantAfter) {
        return instant;
    }

    // Check the local times right before and right after the current instant
    long instantJustBefore = convertUTCToLocal(instant - 1);
    long instantJustAfter = convertUTCToLocal(instant + 1);

    // Determine if there is a transition exactly at the given instant
    if (instantJustBefore != instantJustAfter) {
        // Here, there's a timezone transition at 'instant'
        if (earlierOrLater) {
            // If 'earlierOrLater' is true, choose the instant just after the transition
            return convertLocalToUTC(instantJustAfter, false, instant);
        } else {
            // If 'earlierOrLater' is false, choose the instant just before the transition
            return convertLocalToUTC(instantJustBefore, false, instant);
        }
    }

    // Default case to handle unexpected behavior
    return instant;
}
