public int getOffsetFromLocal(long instantLocal) {
    // Get the initial offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // Adjust instantLocal using the estimate and recalculate the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);

    // If the offsets differ, we must be near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // Check if we are dealing with a backward transition (end of DST)
        long prevTransition = previousTransition(instantAdjusted);

        // If the previous transition is exactly the adjusted instant, 
        // it means we're at the boundary point where the transition happens.
        if (prevTransition == instantAdjusted) {
            // Return the offset that applies just after the transition
            // since we should assume the transition has just happened.
            return getOffset(instantAdjusted + 1);
        } else {
            // Find the next transition time after the adjusted instant
            long nextTransition = nextTransition(instantAdjusted);

            // If the next transition is not the same as the local adjusted instant,
            // and the adjusted instant is before the next transition,
            // it implies we are in the duplicated hour and should use the earlier offset.
            if (nextTransition != instantAdjusted && instantAdjusted < nextTransition) {
                // Check if the local time falls within the overlap by comparing it with the next DST transition
                if (instantLocal < nextTransition + offsetAdjusted) {
                    return offsetAdjusted;
                }
            }
        }
    }

    // In cases where we are not around a DST transition or the conditions above are not met,
    // return the adjusted offset.
    return offsetAdjusted;
}