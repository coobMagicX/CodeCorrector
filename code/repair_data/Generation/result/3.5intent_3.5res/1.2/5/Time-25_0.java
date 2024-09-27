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
                return offsetLocal;
            }
        } else {
            // if the offsetLocal is greater than offsetAdjusted, it means that
            // the time just after the DST transition is not correctly calculated
            // we need to find the previous transition and return the offset at that time
            long previousLocal = previousTransition(instantAdjusted);
            long previousAdjusted = previousTransition(instantLocal - offsetLocal);
            if (previousLocal != previousAdjusted) {
                return getOffset(previousLocal);
            } else {
                // if the previousLocal is the same as previousAdjusted, it means that
                // the time just before the DST transition is not correctly calculated
                // we need to find the next transition and return the offset at that time
                long nextLocal = nextTransition(instantAdjusted);
                long nextAdjusted = nextTransition(instantLocal - offsetLocal);
                if (nextLocal != nextAdjusted) {
                    return getOffset(nextLocal);
                }
            }
        }
    }
    return offsetAdjusted;
}