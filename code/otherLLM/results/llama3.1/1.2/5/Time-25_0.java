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
        long nextLocal = nextTransition(instantAdjusted);
        long nextAdjusted = nextTransition(instantLocal - offsetAdjusted);
        if ((nextLocal != nextAdjusted)) { // Corrected condition to check whether the transition dates differ
            // if we just return offsetAdjusted then the time is pushed
            // back before the transition, whereas it should be
            // on or after the transition
            long prevTransition = previousTransition(instantAdjusted); // Added method call to get previous transition date
            if (prevTransition != nextLocal) {
                return offsetLocal;
            }
        }
    }
    return offsetAdjusted;
}