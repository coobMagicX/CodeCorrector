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
                // Fix: Ensure that time is always after the DST gap by returning the maximum of offsetLocal and offsetAdjusted
                return Math.max(offsetLocal, offsetAdjusted);
            }
        } else {
            // If offsets are equal but instantLocal - offsetLocal is negative, it means we're before a DST boundary transition
            long nextTransitionBefore = previousTransition(instantLocal - offsetLocal);
            if (instantLocal < nextTransitionBefore) {
                return Math.max(offsetLocal, offsetAdjusted);
            }
        }
    }
    return offsetAdjusted;
}