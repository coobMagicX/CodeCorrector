public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalculate the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);
    // Check if the offsets differ, indicating proximity to a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // Calculate the next transition point after the adjusted instant
        long nextLocal = nextTransition(instantAdjusted);
        long nextAdjusted = nextTransition(instantLocal - offsetAdjusted);
        // Check if the transition points differ
        if (nextLocal != nextAdjusted) {
            // Return the local offset if the transition points differ
            return offsetLocal;
        }
    } else {
        // Handle positive offsets
        if (offsetLocal > 0) {
            long prev = previousTransition(instantAdjusted);
            // Check if the adjusted instant is after the previous transition
            if (prev < instantAdjusted) {
                int offsetPrev = getOffset(prev);
                int diff = offsetPrev - offsetLocal;
                // Ensure the time is after the transition by checking the difference
                if (instantAdjusted - prev <= diff) {
                    return offsetPrev;
                }
            }
        }
    }
    // Return the adjusted offset by default
    return offsetAdjusted;
}