public int getOffsetFromLocal(long instantLocal) {
    final int offsetLocal = getOffset(instantLocal);
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);

    if (offsetLocal != offsetAdjusted) {
        // If the offsets differ, we must be near a DST boundary
        // We need to ensure that time is always after the DST gap
        if ((offsetLocal - offsetAdjusted) < 0) {
            long nextLocal = nextTransition(instantAdjusted);
            long nextAdjusted = nextTransition(instantLocal - offsetAdjusted);
            if (nextLocal != nextAdjusted) {
                return offsetLocal;
            }
        } else {
            // For positive offsets, we naturally fall on or after the transition
            return offsetAdjusted;
        }
    } else if (offsetLocal > 0) {
        long prev = previousTransition(instantAdjusted);
        if (prev < instantAdjusted) {
            int offsetPrev = getOffset(prev);
            int diff = offsetPrev - offsetLocal;
            if (instantAdjusted - prev <= diff) {
                return offsetPrev;
            }
        } else {
            // For negative offsets, we need to adjust the transition logic
            long nextPrev = previousTransition(instantLocal - offsetAdjusted);
            if (nextPrev < instantLocal - offsetAdjusted) {
                int offsetNextPrev = getOffset(nextPrev);
                int diff = offsetNextPrev - offsetAdjusted;
                if (instantLocal - nextPrev <= diff) {
                    return offsetNextPrev;
                }
            }
        }
    }

    // If all else fails, return the adjusted offset
    return offsetAdjusted;
}