public long adjustOffset(long instant, boolean earlierOrLater) {
    // Calculate the local date-time for 3 hours before and after the given instant
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);

    // If no transition effect (either a gap or normal), just return the instant
    if (instantBefore == instantAfter) {
        return instant;  
    }

    // Calculate the local time at the given instant
    long targetLocal = convertUTCToLocal(instant);

    // Determine the transition window with duplicate local times
    long transitionStart = instant - 3 * DateTimeConstants.MILLIS_PER_HOUR;
    long transitionEnd = instant + 3 * DateTimeConstants.MILLIS_PER_HOUR;

    while (transitionStart + 1 < transitionEnd) {
        long midPoint = (transitionStart + transitionEnd) / 2;
        long localMidPoint = convertUTCToLocal(midPoint);
        if (localMidPoint <= targetLocal) {
            transitionStart = midPoint;
        } else {
            transitionEnd = midPoint;
        }
    }
    
    // Choose the correct point based on earlierOrLater flag
    long resultInstant = earlierOrLater ? transitionEnd : transitionStart;
    // Check and correct the selected point if it does not return the expected local
    long resultLocal = convertUTCToLocal(resultInstant);
    if (resultLocal != targetLocal) {
        if (earlierOrLater) {
            resultInstant = transitionStart;
        } else {
            resultInstant = transitionEnd;
        }
    }
    return resultInstant;
}
