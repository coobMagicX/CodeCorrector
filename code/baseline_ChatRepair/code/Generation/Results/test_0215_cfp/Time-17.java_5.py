public long adjustOffset(long instant, boolean earlierOrLater) {
    // Calculate the local timestamps before and after the suspected overlap
    long instantBefore = convertUTCToLocal(instant - DateTimeConstants.MILLIS_PER_HOUR * 2); // 2 hours should be enough to clear any overlap
    long instantAfter = convertUTCToLocal(instant + DateTimeConstants.MILLIS_PER_HOUR * 2);  // This covers the range of overlap

    // If the local times are the same before and after the input time, no overlap occurs
    if (instantBefore == instantAfter) {
        return instant;  // No adjustment needed because there is no ambiguity about the offset
    }

    // If an overlap is detected, determine the correct offset based on earlierOrLater
    if (earlierOrLater) {
        // Want the later offset (i.e., the local time that corresponds to instantAfter in UTC)
        return convertLocalToUTC(instant, true, instantAfter);
    } else {
        // Want the earlier offset (i.e., the local time that corresponds to instantBefore in UTC)
        return convertLocalToUTC(instant, true, instantBefore);
    }
}
