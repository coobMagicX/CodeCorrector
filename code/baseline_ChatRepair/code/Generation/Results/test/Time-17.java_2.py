public long adjustOffset(long instant, boolean earlierOrLater) {
    // Evaluate the local time at the instant and instant adjusted for potential overlap duration (typical DST shift is less than 3 hours)
    long instantBefore = convertUTCToLocal(instant - 2 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 2 * DateTimeConstants.MILLIS_PER_HOUR);

    // If the local times at instantBefore and instantAfter are the same, there's no overlap (DST change or other irregularities).
    if (instantBefore == instantAfter) {
        return instant;  // No overlap, return the original instant.
    }

    // Otherwise, we are in an overlap situation. Obtain local time mappings to see which corresponds to the earlier or later UTC time.
    long localTime = convertUTCToLocal(instant);

    // We need to find out if 'instant' is in the part of the overlap that repeats. 
    long earlierMapping = convertLocalToUTC(localTime, true, instant - DateTimeConstants.MILLIS_PER_HOUR);
    long laterMapping = convertLocalToUTC(localTime, false, instant - DateTimeConstants.MILLIS_PER_HOUR);

    // Return either the earlier or later mapping based on the flag.
    if (earlierOrLater) {
        // Choose the later offset (further in time in terms of UTC).
        return laterMapping;
    } else {
        // Choose the earlier offset.
        return earlierMapping;
    }
}
