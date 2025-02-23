public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);

    // Adjust instantLocal using the estimate and recalculate the offset
    long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);

    // If the offsets differ, we are near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // Need to ensure the resulting time is always after the DST gap
        // This generally happens with positive offsets, but negative
        // offsets need correction.
        if (offsetLocal > offsetAdjusted) {
            // After reestimating, if the event should have rolled forward over the transition,
            // then the second offset is the correct option since the original guess was too early.
            return offsetAdjusted;
        } else {
            // Otherwise, return the original look-up when offset is rolling back
            // This would uphold the principle of least surprise.
            return offsetLocal;
        }
    }

    return offsetLocal;
}
