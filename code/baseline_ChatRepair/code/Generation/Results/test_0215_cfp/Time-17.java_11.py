public long adjustOffset(long instant, boolean earlierOrLater) {
    long oneHour = DateTimeConstants.MILLIS_PER_HOUR;
    long threeHours = 3 * oneHour;

    // Computing local times three hours before and after the instant
    long instantBefore = convertUTCToLocal(instant - threeHours);
    long instantAfter = convertUTCToLocal(instant + threeHours);

    // Check if local times one hour before and after are the same or not
    if (instantBefore == instantAfter) {
        // no overlap or gap detected
        return instant;
    }

    // There is an overlap or a gap
    // Determining the range of different local times that could match this instant
    long startOfOverlap = convertLocalToUTC(instantBefore, false);  // During overlap, using earlier mapping
    long endOfOverlap = convertLocalToUTC(instantAfter, true);  // During overlap, using later mapping

    if (earlierOrLater) {
        // Prefer later offset (end of calculated overlap)
        return endOfOverlap;
    } else {
        // Prefer earlier offset (start of calculated overlap)
        return startOfOverlap;
    }
}
