public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalc the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);

    // if the offsets differ, we must be near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // Determine which transition to use: before or after the gap
        long instantAfterGap = nextTransition(instantAdjusted);
        long instantBeforeGap = previousTransition(instantAdjusted);

        if ((offsetLocal - offsetAdjusted) < 0 && instantAfterGap > instantAdjusted) {
            // If we are in a negative offset, and just after the DST transition,
            // then we should use the 'next' transition instead of the current one
            return getOffset(instantAfterGap);
        } else if ((offsetLocal - offsetAdjusted) > 0 && instantBeforeGap < instantAdjusted) {
            // If we are in a positive offset, and just before the DST transition,
            // then we should use the 'previous' transition instead of the current one
            return getOffset(instantBeforeGap);
        }
    } else if (offsetLocal > 0) {
        long prev = previousTransition(instantAdjusted);
        if (prev < instantAdjusted) {
            int offsetPrev = getOffset(prev);
            int diff = offsetPrev - offsetLocal;
            if (instantAdjusted - prev <= diff) {
                return offsetPrev;
            }
        }
    }

    // If no special DST transition is needed, return the adjusted offset
    return offsetAdjusted;
}