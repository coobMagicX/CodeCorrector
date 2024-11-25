public long adjustOffset(long instant, boolean earlierOrLater) {
    long localTime = convertUTCToLocal(instant);

    // Find the overlapping range by checking before and after instants
    long instantBeforeOverlapStart = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfterOverlapEnd = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);

    if (instantBeforeOverlapStart == localTime || instantAfterOverlapEnd == localTime) {
        // No overlap, return the original instant
        return instant;
    }

    // Calculate the difference in local time for before and after overlap
    long diffBeforeOverlap = Math.abs(localTime - instantBeforeOverlapStart);
    long diffAfterOverlap = Math.abs(localTime - instantAfterOverlapEnd);

    if (diffBeforeOverlap < diffAfterOverlap) {
        // Overlap is earlier, apply earlier offset
        return convertLocalToUTC(localTime, false, instantBeforeOverlapStart);
    } else {
        // Overlap is later, apply later offset
        return convertLocalToUTC(localTime, false, instantAfterOverlapEnd);
    }
}