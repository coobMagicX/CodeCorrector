public long adjustOffset(long instant, boolean earlierOrLater) {
    // a bit messy, but will work in all non-pathological cases
    
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    
    // Corrected condition for determining overlap, considering DST transitions
    if (instantBefore <= instantAfter) {
        if (instantBefore == instantAfter) {
            return instant;  // not an overlap (less than is a gap, equal is normal case)
        }
        
        // work out range of instants that have duplicate local times
        long local = convertUTCToLocal(instant);
        
        // Corrected calculation to identify overlaps in different time zones and DST conditions
        if (earlierOrLater) {
            return convertLocalToUTC(local, false, instantAfter);
        } else {
            return convertLocalToUTC(local, false, instantBefore);
        }
    }
    
    // If the condition for determining overlap is not met, it means we have an overlap
    // Calculate result based on whether we are in a later or earlier offset
    if (instantBefore > instantAfter) {
        long temp = instantBefore;
        instantBefore = instantAfter;
        instantAfter = temp;
        
        return convertLocalToUTC(convertUTCToLocal(instant), false, instantBefore);
    }
    
    // If we are in the same time zone but with a DST transition
    else if (instantBefore == instant + DateTimeConstants.MILLIS_PER_HOUR) {
        return convertLocalToUTC(convertUTCToLocal(instant), false, instantAfter);
    } else {
        return convertLocalToUTC(convertUTCToLocal(instant), false, instantBefore);
    }
}