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
            if (nextLocal != nextAdjusted) {
                // To fix handling of negative offsets and the issue with `nextTransition` returning different results,
                // we need to find the actual DST boundary. We can do this by checking which transition is earlier.
                long earliestTransition = Math.min(nextLocal, nextAdjusted);
                return getOffset(earliestTransition + offsetLocal - offsetAdjusted);
            }
        } else {
            // If the offsets differ but the previous condition was not met (i.e., we have a positive offset difference),
            // then simply return the adjusted offset.
            return offsetAdjusted;
        }
    }
    return offsetAdjusted;
}