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
            // If the difference between the original and adjusted offset is negative,
            // it means we are past the transition point. In this case, we should
            // check if the next transition after instantAdjusted is indeed at or after
            // the transition that would have occurred at instantLocal.

            long nextLocal = nextTransition(instantAdjusted);
            long instantAfterGap = instantAdjusted + (nextLocal - instantAdjusted);

            // If the next transition found after adjusting for the DST gap is not the same
            // as the one we found at instantAdjusted, it means we are on the other side of the transition.
            if (nextLocal != instantAfterGap) {
                return offsetLocal;
            }
        } else {
            // If the difference is positive, we have crossed a DST boundary and need to adjust.
            long nextTransitionBefore = nextTransition(instantAdjusted);
            // Since we are on the other side of the transition, subtracting
            // the difference between the two times should give us back the original offset.
            return offsetLocal - (instantAdjusted - nextTransitionBefore);
        }
    }
    return offsetAdjusted;
}