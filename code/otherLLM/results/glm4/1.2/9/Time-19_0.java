public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalc the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);

    if (offsetLocal != offsetAdjusted) {
        // Determine if we're near a DST boundary by checking if the offsets differ
        if ((offsetLocal - offsetAdjusted) < 0) {
            // Ensure time is after the DST gap, this only needs adjustment for negative offsets
            long nextLocal = nextTransition(instantAdjusted);
            long prevAdjusted = previousTransition(instantLocal - offsetAdjusted);

            // If the next transition from the adjusted instant is different from that of the original,
            // it implies a DST change has occurred.
            if (nextTransition(prevAdjusted) != nextLocal) {
                return offsetLocal;
            }
        }
    } else if (offsetLocal > 0 && instantLocal - previousTransition(instantAdjusted) <= getOffset(previousTransition(instantAdjusted)) - offsetLocal) {
        // If the current instant is within a DST gap, and it's before the transition, use the previous offset
        return getOffset(previousTransition(instantAdjusted));
    }

    // Return the adjusted offset if no DST boundary handling was required
    return offsetAdjusted;
}

// You should also ensure that your nextTransition() and previousTransition() methods are correctly implemented to handle DST transitions properly.