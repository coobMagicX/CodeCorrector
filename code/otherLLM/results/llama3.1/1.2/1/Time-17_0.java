public long adjustOffset(long instant, boolean earlierOrLater) {
    // a bit messy, but will work in all non-pathological cases
    
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    
    // check for overlap in the correct direction (i.e., withLaterOffsetAtOverlap)
    if (instantBefore == instantAfter) {
        return instant;  // not an overlap (less than is a gap, equal is normal case)
    }
    
    long local = convertUTCToLocal(instant);
    long result = convertLocalToUTC(local, false, earlierOrLater ? instantAfter : instantBefore);
    
    if (instantBefore == instantAfter) {
        // we've previously established that this isn't an overlap
        // so return the original instant instead of potentially returning a different offset
        return instant;
    }
    
    // calculate result
    return result;
}