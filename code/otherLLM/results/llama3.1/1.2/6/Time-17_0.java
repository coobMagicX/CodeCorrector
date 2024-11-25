public long adjustOffset(long instant, boolean earlierOrLater) {
    // a bit messy, but will work in all non-pathological cases
    
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    
    // handle cases where the input local time is identical to the output UTC time
    if (instantBefore == instantAfter) {
        return instant;  // not an overlap (less than is a gap, equal is normal case)
    }
    
    // work out range of instants that have duplicate local times
    long local = convertUTCToLocal(instant);
    
    // ensure that the `convertLocalToUTC` method handles cases where there are multiple overlaps
    if (instantBefore != instantAfter) {
        return Math.min(convertLocalToUTC(local, true, earlierOrLater ? instantAfter : instantBefore),
                convertLocalToUTC(local, true, earlierOrLater ? instantBefore : instantAfter));
    } else {
        return convertLocalToUTC(local, true, earlierOrLater ? instantBefore : instantAfter);
    }
}