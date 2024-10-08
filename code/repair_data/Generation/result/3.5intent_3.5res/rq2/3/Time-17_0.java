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
    
    // adjust the local time to the earlier or later offset
    if (earlierOrLater) {
        return convertLocalToUTC(local, false, instantAfter);
    } else {
        return convertLocalToUTC(local, true, instantBefore);
    }
}

public long convertUTCToLocal(long instant) {
    // implementation goes here
    // existing method
}

public long convertLocalToUTC(long localInstant, boolean strict, long oldWallOffset) {
    // implementation goes here
    // existing method
}