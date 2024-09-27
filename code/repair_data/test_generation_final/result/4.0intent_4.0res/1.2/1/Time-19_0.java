public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalc the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);

    // if the offsets differ, we must be near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        if ((offsetLocal - offsetAdjusted) < 0) {
            // if we just return offsetAdjusted then the time is pushed
            // back before the transition, whereas it should be
            // on or after the transition
            long nextAdjusted = nextTransition(instantAdjusted);
            long nextLocal = nextTransition(instantLocal - offsetAdjusted);
            if (nextLocal != nextAdjusted) {
                return offsetLocal;
            }
        } else {
            // Handle the case where the calculated local time falls into an overlap
            long prevAdjusted = previousTransition(instantAdjusted);
            if (prevAdjusted < instantAdjusted) {
                int offsetPrev = getOffset(prevAdjusted);
                if (offsetPrev != offsetAdjusted) {
                    long prevLocal = previousTransition(instantLocal - offsetLocal);
                    if (prevAdjusted == prevLocal) {
                        return offsetPrev;
                    }
                }
            }
        }
    } else {
        // Check if there is a previous transition impacting the current offset
        long prev = previousTransition(instantAdjusted);
        if (prev < instantAdjusted) {
            int offsetPrev = getOffset(prev);
            if (offsetPrev != offsetLocal) {
                return offsetPrev;
            }
        }
    }
    return offsetAdjusted;
}