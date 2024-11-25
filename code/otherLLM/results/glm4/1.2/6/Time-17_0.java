public long adjustOffset(long instant, boolean earlierOrLater) {
    long adjustedInstant;
    
    // Use a larger range to detect overlaps properly.
    long offsetBefore = convertUTCToLocal(instant - 24 * DateTimeConstants.MILLIS_PER_HOUR);
    long offsetAfter = convertUTCToLocal(instant + 24 * DateTimeConstants.MILLIS_PER_HOUR);

    if (earlierOrLater) {
        // If we want the earlier time, check for overlaps moving backwards in time.
        adjustedInstant = findOverlap(offsetBefore, instant);
    } else {
        // If we want the later time, check for overlaps moving forward in time.
        adjustedInstant = findOverlap(instant, offsetAfter);
    }

    if (adjustedInstant == instant) {
        return instant;  // No overlap detected, return the original instant.
    } else {
        // Convert the local time to UTC
        long convertedAdjustedInstant;
        try {
            convertedAdjustedInstant = convertLocalToUTC(adjustedInstant, false);
        } catch (DateTimeException e) {
            // Handle any date-time exception that might occur during conversion.
            throw new AssertionError("Error converting local to UTC", e);
        }
        return convertedAdjustedInstant;
    }
}

// Helper method to find an overlap in time zones
private long findOverlap(long start, long end) {
    long overlap = 0;
    while (start < end) {
        // Calculate the next possible overlap by checking if both instants have the same local time.
        long localStart = convertUTCToLocal(start);
        long localEnd = convertUTCToLocal(end);

        if (localStart == localEnd) {
            return start;  // Found an overlap, return the start instant.
        }

        // Move to the next possible overlapping point in time.
        if (start < localEnd) {
            start += DateTimeConstants.MILLIS_PER_HOUR;
        } else {
            end -= DateTimeConstants.MILLIS_PER_HOUR;
        }
    }
    
    // If no overlap is found, return 0 or throw an appropriate exception/error.
    return 0;  // Indicating no overlap was found.
}