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
            long currentTransition = nextTransition(instantLocal);

            // Check if the instant we're dealing with is within the DST gap
            boolean isWithinDSTGap = (instantLocal - currentTransition) < 0;
            
            // If it's within the DST gap and the time should be after the transition,
            // use the original offset.
            if (isWithinDSTGap && nextLocal != instantAdjusted) {
                return offsetLocal;
            }
        }
    }
    return offsetAdjusted;
}