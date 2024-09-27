public long adjustOffset(long instant, boolean earlierOrLater) {
    // a bit messy, but will work in all non-pathological cases
    
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    if (instantBefore == instantAfter) {
        return instant;  // not an overlap (less than is a gap, equal is normal case)
    }
    
    // work out range of instants that have duplicate local times
    long local = convertUTCToLocal(instant);
    long result = convertLocalToUTC(local, false, instantBefore);
    if (earlierOrLater) {
        result = convertLocalToUTC(local, false, instantAfter);
    }
    
    // adjust the result if it falls within the overlapping time period
    if (earlierOrLater) {
        long earlier = convertUTCToLocal(result - DateTimeConstants.MILLIS_PER_HOUR);
        long later = convertUTCToLocal(result + DateTimeConstants.MILLIS_PER_HOUR);
        if (later - earlier == 2 * DateTimeConstants.MILLIS_PER_HOUR) {
            result = convertLocalToUTC(local, true, result);
        }
    } else {
        long earlier = convertUTCToLocal(result + DateTimeConstants.MILLIS_PER_HOUR);
        long later = convertUTCToLocal(result - DateTimeConstants.MILLIS_PER_HOUR);
        if (earlier - later == 2 * DateTimeConstants.MILLIS_PER_HOUR) {
            result = convertLocalToUTC(local, false, result);
        }
    }
    
    return result;
}