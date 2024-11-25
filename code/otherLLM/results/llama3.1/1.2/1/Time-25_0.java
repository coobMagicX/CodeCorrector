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
        long nextLocal = nextTransition(instantLocal);
        long nextAdjusted = nextTransition(instantLocal - offsetAdjusted);
        if (nextLocal != nextAdjusted) {
            // Return the adjusted offset to ensure time is on or after the transition
            return offsetAdjusted;
        }
    }
    return offsetAdjusted;
}