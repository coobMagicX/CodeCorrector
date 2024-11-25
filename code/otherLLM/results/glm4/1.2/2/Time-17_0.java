public long adjustOffset(long instant, boolean earlierOrLater) {
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);

    // Determine the correct offset for the current instant based on whether it's an overlap
    if (instantBefore == instantAfter) {
        return instant; // not an overlap (less than is a gap, equal is normal case)
    } else {
        // Check which direction we need to use based on the earlierOrLater flag
        long local = convertUTCToLocal(instant);
        // If it's overlapping or we are looking for the 'earlier' time, we go with instantBefore
        // Otherwise, we go with instantAfter.
        return earlierOrLater ? getOffsetFromLocal(convertLocalToUTC(local, false, instantAfter))
                              : getOffsetFromLocal(convertLocalToUTC(local, false, instantBefore));
    }
}