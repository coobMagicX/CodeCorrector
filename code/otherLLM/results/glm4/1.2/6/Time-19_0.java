public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalc the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);

    if (offsetLocal != offsetAdjusted) {
        // if we are moving to a lower offset, we need to check the next transition
        if ((offsetLocal - offsetAdjusted) < 0) {
            long nextLocal = nextTransition(instantAdjusted);
            long nextAdjusted = nextTransition(instantLocal - offsetAdjusted);

            // If the next transitions are different, it means the DST boundary is crossed
            if (nextLocal != nextAdjusted) {
                // The original offset should be used to ensure we're after the transition
                return offsetLocal;
            }
        } else {
            // If we are moving to a higher offset and not crossing DST boundary,
            // check if the previous transition could provide a correct offset.
            long prev = previousTransition(instantAdjusted);
            int offsetPrev = getOffset(prev);

            // Calculate the difference between the current adjusted offset and the previous offset
            int diff = offsetPrev - offsetAdjusted;

            // If the instant is within the DST gap, we should use the previous offset
            if (instantAdjusted <= prev + diff) {
                return offsetPrev;
            }
        }
    } else if (offsetLocal > 0) {
        long prev = previousTransition(instantAdjusted);
        if (prev < instantAdjusted) {
            int offsetPrev = getOffset(prev);
            int diff = offsetPrev - offsetLocal;

            // If the instant is within the DST gap, we should use the previous offset
            if (instantAdjusted <= prev + diff) {
                return offsetPrev;
            }
        }
    }

    // If no adjustments are needed, return the adjusted offset
    return offsetAdjusted;
}