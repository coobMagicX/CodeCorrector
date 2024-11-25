public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalc the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);
    
    if (offsetLocal != offsetAdjusted) {
        // If the offsets differ, we must be near a DST boundary.
        // We need to ensure that time is always after the DST gap.
        // This happens naturally for positive offsets, but not for negative.
        
        long nextTransition = nextTransition(instantLocal);
        if (nextTransition - instantAdjusted < 0) {
            // If we just return offsetAdjusted then the time is pushed
            // back before the transition, whereas it should be
            // on or after the transition. So we need to return the correct offset.
            return offsetLocal;
        }
    }
    
    return offsetAdjusted;
}