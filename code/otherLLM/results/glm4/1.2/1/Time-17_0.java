public long adjustOffset(long instant, boolean earlierOrLater) {
    // Evaluate if the overlap period is in effect
    long[] overlapPeriods = getOverlapPeriods(instant);
    long instantBeforeOverlap = overlapPeriods[0];
    long instantAfterOverlap = overlapPeriods[1];

    // If no overlap periods are found, adjust as usual
    if (instantBeforeOverlap == Long.MIN_VALUE && instantAfterOverlap == Long.MIN_VALUE) {
        return convertLocalToUTC(convertUTCToLocal(instant), false, earlierOrLater ? getOffset(instant) : -getOffset(instant));
    }
    
    // If the given instant is during the overlap period
    if (instant >= instantBeforeOverlap && instant < instantAfterOverlap) {
        long localTime = convertUTCToLocal(instant);
        // Determine whether to adjust towards the earlier or later offset
        long adjustedInstant;
        if (earlierOrLater) {
            adjustedInstant = localTime - getOffset(localTime);
        } else {
            adjustedInstant = localTime + getOffset(localTime);
        }
        return adjustedInstant;
    }

    // If the given instant is before the overlap period or after it
    long localTime = convertUTCToLocal(instant);
    if (earlierOrLater) {
        return convertLocalToUTC(localTime, false, instantAfterOverlap > instant ? getOffset(instant) : -getOffset(instant));
    } else {
        return convertLocalToUTC(localTime, false, instantBeforeOverlap < instant ? getOffset(instant) : -getOffset(instant));
    }
}

// Helper method to determine overlap periods
private long[] getOverlapPeriods(long instant) {
    // This method should calculate the start and end of the DST overlap period for a given instant.
    // For simplicity, let's assume this method is implemented elsewhere and returns Long.MIN_VALUE if no overlap is found.
    // long startOverlap = ...;
    // long endOverlap = ...;
    // return new long[]{startOverlap, endOverlap};
    return new long[]{Long.MIN_VALUE, Long.MIN_VALUE};
}

// Helper method to get the offset for a given local time
private int getOffset(long instantLocal) {
    int offset = getOffset(instantLocal - convertUTCToLocal(0));
    // Adjust offset if there was an overflow in conversion
    if ((instantLocal ^ offset) < 0 && (convertUTCToLocal(0) ^ offset) >= 0) {
        throw new ArithmeticException("Adding time zone offset caused overflow");
    }
    return offset;
}