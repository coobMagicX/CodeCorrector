public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalc the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);

    if (offsetLocal != offsetAdjusted) {
        // if the first estimate of the offset is greater, we must be near a DST boundary
        if (offsetLocal > offsetAdjusted) {
            // Determine if the time is before or after the transition by comparing the next transition times
            long nextTransitionBefore = nextTransition(instantLocal);
            long nextTransitionAfter = nextTransition(instantAdjusted);

            if (nextTransitionBefore == nextTransitionAfter) {
                // If the next transition is the same for both, we should return the original offset
                return offsetLocal;
            } else if (nextTransitionBefore < nextTransitionAfter) {
                // We are before the DST boundary; ensure we're on or after the transition by adding the difference to the adjusted offset
                return offsetAdjusted + (offsetLocal - offsetAdjusted);
            }
        }
    }

    // If no DST transition needs to be handled, or if the logic above determines it should not be handled,
    // return the adjusted offset.
    return offsetAdjusted;
}