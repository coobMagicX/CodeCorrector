public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalc the offset
    long instantAdjusted = instantLocal - offsetLocal;
    int offsetAdjusted = getOffset(instantAdjusted);

    // if the offsets differ, check if we are dealing with a transition
    if (offsetLocal != offsetAdjusted) {
        // Calculate transition points
        long nextTransition = nextTransition(instantAdjusted);
        long prevTransition = previousTransition(instantAdjusted);

        // In cases where instantAdjusted is exactly a transition time,
        // it should not underflow into the previous offset.
        if (instantAdjusted == nextTransition) {
            // The adjusted instant points exactly on the transition, use the new offset
            return offsetAdjusted;
        } else if (instantAdjusted == prevTransition) {
            // The adjusted instant points exactly on the previous transition, use the local offset
            return offsetLocal;
        }

        // Check if the adjusted instant falls within the validation range.
        if (nextTransition < instantLocal) {
            if (prevTransition != nextTransition && nextTransition - instantAdjusted > offsetAdjusted) {
                // We are just past a transition point but incorrectly adjusted into the prior period.
                // Use the offset before the transition because the local instant implies a time after the transition.
                return offsetLocal;
            }
        }
    }
    return offsetAdjusted;
}
