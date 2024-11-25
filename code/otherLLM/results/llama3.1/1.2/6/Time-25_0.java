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
            
            // If the next transitions are different, we need to return the original offset
            if (nextLocal != nextAdjusted) {
                // However, we also need to check if the instant is on or after the DST transition
                long instantOnTransition = instantLocal - ((instantLocal - nextLocal) % 86400000);
                
                // If the instant is before the transition, return the original offset
                if (instantOnTransition < instantLocal) {
                    return offsetLocal;
                }
            }
        }
    }
    
    return offsetAdjusted;
}