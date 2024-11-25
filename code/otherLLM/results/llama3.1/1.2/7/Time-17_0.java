public long adjustOffset(long instant, boolean earlierOrLater) {
    // a bit messy, but will work in all non-pathological cases
    
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    
    // work out range of instants that have duplicate local times
    long local = convertUTCToLocal(instant);
    if (instantBefore == instantAfter) {
        return instant;  // not an overlap (less than is a gap, equal is normal case)
    }
    
    // correct handling of earlier/later offset and proper calculation of later offset at overlap
    long adjustedInstant = local;
    if ((earlierOrLater && local > instantBefore && local < instantAfter) || (!earlierOrLater && local < instantBefore && local > instantAfter)) {
        adjustedInstant = convertLocalToUTC(local, false, earlierOrLater ? instantAfter : instantBefore);
    }
    
    return adjustedInstant;
}