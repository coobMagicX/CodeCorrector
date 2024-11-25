public int getOffsetFromLocal(long instantLocal) {
    // Get the initial local offset
    final int offsetLocal = getOffset(instantLocal);
    // Adjust instantLocal using the estimate and recalculate the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);

    // If the offsets differ, we must be near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // Ensure that time is always after the DST gap
        if ((offsetLocal - offsetAdjusted) < 0) {
            // Calculate the next transition after the adjusted instant
            long nextAdjusted = nextTransition(instantAdjusted);
            // If the next transition for the adjusted instant is different, use the local offset
            if (nextAdjusted != instantAdjusted) {
                return offsetLocal;
            }
        }
    } else if (offsetLocal > 0) {
        // Find the previous transition before the adjusted instant
        long prev = previousTransition(instantAdjusted);
        if (prev < instantAdjusted) {
            int offsetPrev = getOffset(prev);
            int diff = offsetPrev - offsetLocal;
            // Check if the time is within the DST gap and adjust to the previous offset
            if (instantAdjusted - prev <= diff) {
                return offsetPrev;
            }
        }
    }

    // Return the adjusted offset as a result of the method
    return offsetAdjusted;
}