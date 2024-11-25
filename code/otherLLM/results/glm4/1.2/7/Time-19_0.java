public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalc the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);

    if (offsetLocal != offsetAdjusted) {
        // if offsets differ, we're near a DST boundary. We need to find the correct one.
        long nextInstantAfterDSTChange = nextTransition(instantAdjusted);
        long prevInstantBeforeDSTChange = previousTransition(instantAdjusted);

        if ((instantAdjusted - prevInstantBeforeDSTChange) < (nextInstantAfterDSTChange - instantAdjusted)) {
            // We're before the DST transition, so we need to use the earlier offset.
            return getOffset(prevInstantBeforeDSTChange);
        }
    } else if (offsetLocal > 0) {
        // If the local offset is positive, and there's a previous transition,
        // check if the current instant is within the DST gap or just after it.
        long prev = previousTransition(instantAdjusted);
        if (prev < instantAdjusted) {
            int offsetPrev = getOffset(prev);
            int diff = offsetPrev - offsetLocal;
            if (instantAdjusted - prev <= diff) {
                // If the current instant is within the DST gap, use the earlier offset.
                return offsetPrev;
            }
        }
    }

    return offsetAdjusted;
}