public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalc the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);
    // if the offsets differ, we must be near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // we need to ensure that time is always after the DST gap
        // this happens naturally for positive offsets, but not for negative
        if ((offsetLocal - offsetAdjusted) < 0) {
            // if we just return offsetAdjusted then the time is pushed
            // back before the transition, whereas it should be
            // on or after the transition
            long nextLocal = nextTransition(instantAdjusted);
            long nextAdjusted = nextTransition(instantLocal - offsetAdjusted);
            if (nextLocal != nextAdjusted) {
                return Math.max(offsetLocal, offsetAdjusted); // <--- FIX: Return max of offsets
            }
        } else { // Also consider the case where offsetLocal is negative
            long prev = previousTransition(instantAdjusted);
            if (prev < instantAdjusted) {
                int offsetPrev = getOffset(prev);
                int diff = offsetPrev - offsetLocal;
                if (instantAdjusted - prev <= diff) {
                    return Math.min(offsetLocal, offsetAdjusted); // <--- FIX: Return min of offsets
                }
            } else { // Also consider the case where instantAdjusted is exactly at a transition point
                long next = nextTransition(instantAdjusted);
                int offsetNext = getOffset(next);
                if (next != instantAdjusted) {
                    return Math.min(offsetLocal, offsetAdjusted); // <--- FIX: Return min of offsets
                }
            }
        }
    } else if (offsetLocal > 0) { // Also consider the case where offsetLocal is positive
        long prev = previousTransition(instantAdjusted);
        if (prev < instantAdjusted) {
            int offsetPrev = getOffset(prev);
            int diff = offsetPrev - offsetLocal;
            if (instantAdjusted - prev <= diff) {
                return Math.min(offsetLocal, offsetAdjusted); // <--- FIX: Return min of offsets
            }
        }
    }
    return offsetAdjusted;
}