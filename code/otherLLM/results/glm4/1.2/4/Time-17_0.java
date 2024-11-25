public long adjustOffset(long instant, boolean earlierOrLater) {
    // First, convert the given instant to local time
    long local = convertUTCToLocal(instant);
    
    // Check if there is a daylight saving time gap or overlap at this local time
    if (isLocalDateTimeGap(LocalDateTime.ofInstant(Instant.ofEpochMilli(local), ZoneId.systemDefault()))) {
        // If there is an overlap or gap, calculate the actual offset for this instant
        int adjustedOffset = getOffsetFromLocal(local);
        
        // Adjust the local time to account for the DST overlap/gap
        long adjustedLocalTime = instant - adjustedOffset;
        
        // Determine the direction of adjustment needed based on earlierOrLater flag
        if (earlierOrLater) {
            // If we are looking for a later offset, find the next transition after the given instant
            return convertLocalToUTC(nextTransition(adjustedLocalTime), false, true);
        } else {
            // If we are looking for an earlier offset, find the previous transition before the given instant
            return convertLocalToUTC(previousTransition(adjustedLocalTime), false, false);
        }
    } else {
        // If there is no DST overlap or gap, simply adjust the local time to UTC based on the provided instant
        return convertLocalToUTC(local, false, earlierOrLater ? local : instant);
    }
}