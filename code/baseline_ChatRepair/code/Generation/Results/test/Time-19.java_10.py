public int getOffsetFromLocal(long instantLocal) {
    int offsetLocal = getOffset(instantLocal);
    long instantAdjusted = instantLocal - offsetLocal;
    int offsetAdjusted = getOffset(instantAdjusted);

    // Check if there was an adjustment due to DST and resolve ambiguity if needed.
    if (offsetLocal != offsetAdjusted) {
        // Calculate the local time instant assuming the adjusted offset
        long instantLocalFromAdjusted = instantLocal - offsetAdjusted;
        // Get the offset for this adjusted instant
        int offsetOfAdjusted = getOffset(instantLocalFromAdjusted);
        
        if (offsetOfAdjusted == offsetAdjusted) {
            return offsetAdjusted;
        }
        
        // Determine overlap by checking for DST transition around the adjusted instant
        long prevTransition = previousTransition(instantAdjusted);
        long nextTransition = nextTransition(instantAdjusted);

        // Check if instantLocal is in the overlapped duration and select the later (post-DST) offset
        if (instantAdjusted >= prevTransition && instantAdjusted < nextTransition) {
            if (instantLocal - offsetLocal < nextTransition - offsetLocal && 
                instantLocal - offsetLocal >= prevTransition - offsetLocal) {
                return Math.min(offsetAdjusted, offsetLocal);
            }
        }
    }

    // If there is no ambiguity from offset adjustments, or transitions are fine, return baseline adjusted offset
    return offsetAdjusted;
}
