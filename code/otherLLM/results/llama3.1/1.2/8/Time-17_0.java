public long adjustOffset(long instant, boolean earlierOrLater) {
    // a bit messy, but will work in all non-pathological cases
    
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    
    // Check for overlap by comparing local times before and after adjustment
    if (instantBefore == instantAfter) {
        return instant;  // not an overlap (less than is a gap, equal is normal case)
    }
    
    // work out range of instants that have duplicate local times
    long local = convertUTCToLocal(instant);
    
    // Calculate the offset difference between before and after adjustments
    int offsetDiff = getOffset(convertUTCToLocal(instant + DateTimeConstants.MILLIS_PER_HOUR)) - getOffset(convertUTCToLocal(instant - DateTimeConstants.MILLIS_PER_HOUR));
    
    // calculate result
    return convertLocalToUTC(local, false, earlierOrLater ? instantAfter : instantBefore) + offsetDiff;
}