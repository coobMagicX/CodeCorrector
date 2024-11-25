public int getOffsetFromLocal(long instantLocal) {
    // Get the initial estimate of the offset at instantLocal
    final int offsetLocal = getOffset(instantLocal);
    // Adjust instantLocal using this estimate and recalculate the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);

    // If the offsets differ, we must be near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // Ensure that time is always after the DST gap. This happens naturally for positive offsets,
        // but not for negative. We need to find the next transition and check if it's after the adjusted instant.
        long nextTransition = nextTransition(instantAdjusted);
        
        // Determine the correct offset based on the next transition
        int correctOffset = (nextTransition > instantLocal) ? getOffset(nextTransition) : offsetAdjusted;

        return correctOffset;
    } else if (offsetLocal > 0) {
        // If the offset is positive, check for a previous transition
        long prevTransition = previousTransition(instantAdjusted);
        if (prevTransition < instantAdjusted && prevTransition >= 0) {
            int offsetPrev = getOffset(prevTransition);
            // Determine which offset is correct based on how close the adjusted time is to the previous transition
            int correctOffset = (instantAdjusted - prevTransition <= offsetPrev - offsetLocal)
                ? offsetPrev : offsetAdjusted;
            
            return correctOffset;
        }
    }

    // If no DST adjustment is needed, return the adjusted offset
    return offsetAdjusted;
}