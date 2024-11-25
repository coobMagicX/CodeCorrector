public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalc the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);

    // if the offsets differ, we must be near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // Check if we are moving towards a DST transition that would have already passed
        boolean isMovingBackwards = (offsetLocal - offsetAdjusted) < 0;
        
        // We need to ensure that time is always after the DST gap
        // this happens naturally for positive offsets, but not for negative
        if (!isMovingBackwards || nextTransition(instantAdjusted) != Long.MIN_VALUE) {
            long nextLocal = nextTransition(instantAdjusted);
            long nextAdjusted = nextTransition(instantLocal - offsetAdjusted);

            // If the transitions are different, we need to adjust the offset
            if (nextLocal != nextAdjusted) {
                return isMovingBackwards ? offsetAdjusted : offsetLocal;
            }
        } else {
            // If no transition can be found, we should use the original offset
            return offsetLocal;
        }
    }

    // If the offsets are the same or adjustments are not needed, just return the adjusted offset
    return offsetAdjusted;
}

// Note: The `nextTransition` method is assumed to return Long.MIN_VALUE if no transition can be found.