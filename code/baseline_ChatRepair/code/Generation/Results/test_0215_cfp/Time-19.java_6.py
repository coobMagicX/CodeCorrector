public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalc the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);

    // if the offsets differ, we must be near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // Determine the next transition after the estimated adjusted timestamp
        long nextTransition = nextTransition(instantAdjusted);
        
        // Check if the original local timestamp is before the next transition
        if (instantLocal < nextTransition) {
            return offsetLocal;
        } else {
            return offsetAdjusted;
        }
    } else {
        // Check periods around standard offset transitions
        if (offsetLocal > 0) {
            long previousTransition = previousTransition(instantAdjusted);
            if (instantAdjusted - previousTransition < offsetLocal) {
                return getOffset(previousTransition);
            }
        }
    }
    return offsetAdjusted;
}
