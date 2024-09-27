public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalc the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);
    // if the offsets differ, we must be near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // Calculate the next transition for both the adjusted and the local instant
        long nextTransitionInstantLocal = nextTransition(instantLocal);
        long nextTransitionInstantAdjusted = nextTransition(instantAdjusted);

        // Determine if we are before or after the transition
        if (nextTransitionInstantLocal != nextTransitionInstantAdjusted) {
            // If the calculated transition times are different, this means the original instant is around a transition point
            // We need to ensure we use the correct offset to place the time after the DST transition
            // Check which transition is later and choose the corresponding offset
            if (instantAdjusted < nextTransitionInstantAdjusted && instantLocal >= nextTransitionInstantLocal) {
                // If instantAdjusted is before its next transition but instantLocal is after its transition,
                // this means instantLocal is correctly adjusted past the DST transition
                return offsetLocal;
            } else if (instantLocal < nextTransitionInstantLocal) {
                // If instantLocal is before the transition, use the adjusted offset to ensure it is after the transition
                return offsetAdjusted;
            }
        }
    }
    return offsetAdjusted;
}