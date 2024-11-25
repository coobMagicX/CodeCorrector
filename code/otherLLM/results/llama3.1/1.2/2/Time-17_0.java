public long adjustOffset(long instant, boolean earlierOrLater) {
    // a bit messy, but will work in all non-pathological cases
    
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    
    // Check for multiple overlaps by comparing the before and after instants
    if (instantBefore == instant) {
        return instant;  // not an overlap (less than is a gap, equal is normal case)
    } else if (instantAfter == instant) {
        // If there's only one overlap, check if it's in the earlier or later offset
        return convertLocalToUTC(convertUTCToLocal(instant), false, earlierOrLater ? instantBefore : instant);
    }
    
    // If there are multiple overlaps, find the range of instants with duplicate local times
    long local = convertUTCToLocal(instant);
    
    // Calculate the earliest and latest overlap instants
    long minOverlap = instant;
    long maxOverlap = instant;
    for (long i = instant - 3 * DateTimeConstants.MILLIS_PER_HOUR; i <= instant + 3 * DateTimeConstants.MILLIS_PER_HOUR; i++) {
        if (convertUTCToLocal(i) == local) {
            minOverlap = Math.min(minOverlap, i);
            maxOverlap = Math.max(maxOverlap, i);
        }
    }
    
    // Return the adjusted offset in the specified direction
    return convertLocalToUTC(local, false, earlierOrLater ? maxOverlap : minOverlap);
}