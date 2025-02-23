public long adjustOffset(long instant, boolean earlierOrLater) {
    // Get the offset three hours before and after to detect any changes around the instant
    long threeHours = 3 * DateTimeConstants.MILLIS_PER_HOUR;
    long offsetBefore = getOffset(convertUTCToLocal(instant - threeHours));
    long offsetAfter = getOffset(convertUTCToLocal(instant + threeHours));

    // If these offsets are the same, no transition at or around `instant`, so return it directly
    if (offsetBefore == offsetAfter) {
        return instant;
    }

    // Find the local times for the exact moment and one millisecond before it
    long instantLocal = convertUTCToLocal(instant);
    long instantLocalBefore = convertUTCToLocal(instant - 1);

    // Using offset at the calculated local times to determine the surrounding environment of transition
    long offsetAtInstant = getOffset(instantLocal);
    long offsetJustBefore = getOffset(instantLocalBefore);

    // If the offset changes exactly at `instant` or there is a repeat of local time
    if (offsetAtInstant != offsetJustBefore) {
        if (earlierOrLater) {
            // Select later offset typically resulting from "falling back"
            return convertLocalToUTC(instantLocal, true, instant);
        } else {
            // Select earlier offset typically resulting from "springing forward"
            return convertLocalToUTC(instantLocal, false, instant);
        }
    } else {
        // In normal cases, no transition effects, return the passed `instant`
        return instant;
    }
}

// Methods for getting offset given local millisecond timestamp (helper methods needed depending on your implementation)
private long getOffset(long localInstant) {
    // You need to implement this method based on how you store timezone rules in your system 
    // or use from your library if available. It should return the offset in millis.
}

// Utility to shift from UTC to local considering an offset
private long convertUTCToLocal(long utcMilli) {
    long offset = getOffset(utcMilli);  // You might need to adjust how you retrieve the offset
    return utcMilli + offset;
}

// Utility to shift from local to UTC by specifying the desired behavior during ambiguities
private long convertLocalToUTC(long localMilli, boolean strict, long transitionInstant) {
    long offset = getOffset(transitionInstant);
    return localMilli - offset;
}
