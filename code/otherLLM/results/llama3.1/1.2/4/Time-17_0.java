public long adjustOffset(long instant, boolean earlierOrLater) {
    // a bit messy, but will work in all non-pathological cases
    
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    
    // correct the logic for determining overlaps and handle edge cases
    if (instantBefore == instantAfter) {
        return instant;  // not an overlap (less than is a gap, equal is normal case)
    }
    
    long local = convertUTCToLocal(instant);
    
    // calculate result in the current offset
    long resultInCurrentOffset = convertLocalToUTC(local, false, earlierOrLater ? instantAfter : instantBefore);
    
    // adjust the result based on whether we are switching from an earlier to a later offset, or vice versa
    if (earlierOrLater) {
        return resultInCurrentOffset;
    } else {
        long currentOffset = getOffset(instant);
        long previousOffset = getOffset(resultInCurrentOffset - 1);
        return convertLocalToUTC(local + (currentOffset - previousOffset), false, earlierOrLater ? instantAfter : instantBefore);
    }
}