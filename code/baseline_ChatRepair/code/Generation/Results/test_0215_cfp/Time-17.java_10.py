public long adjustOffset(long instant, boolean earlierOrLater) {
    // Definitions for 1 hour before and after the target instant
    long oneHour = DateTimeConstants.MILLIS_PER_HOUR;
    
    // Determine the local times for 1 hour before, the instant itself, and 1 hour after the given instant
    long instantMinusOneHour = convertUTCToLocal(instant - oneHour);
    long instantPlusOneHour = convertUTCToLocal(instant + oneHour);
    long targetLocal = convertUTCToLocal(instant);

    // If local time doesn't change over these 3 hours, there is no overlap or gap
    if (instantMinusOneHour == instantPlusOneHour) {
        return instant;  // Normal behavior without DST overlap
    }

    // Detect if there is an overlap at the given instant
    boolean overlap = (instantPlusOneHour - instantMinusOneHour) < (2 * oneHour);

    if (overlap) {
        // If there was an overlap, select the appropriate UTC instant based on earlierOrLater
        return convertLocalToUTC(targetLocal, false, earlierOrLater ? instantPlusOneHour : instantMinusOneHour);
    } else {
        // No overlap detected; directly return the given instant
        return instant;
    }
}
