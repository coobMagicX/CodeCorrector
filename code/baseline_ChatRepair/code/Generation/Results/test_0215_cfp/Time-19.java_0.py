public int getOffsetFromLocal(long instantLocal) {
    // Get the offset at local instant first estimate
    int offsetLocal = getOffset(instantLocal);
    // Adjust instantLocal using the first estimate and recalculate the offset
    long instantAdjusted = instantLocal - offsetLocal;
    int offsetAdjusted = getOffset(instantAdjusted);
    
    // Check if the two offsets are different, possibly due to a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // Calculate again to find the actual transition time for DST
        long instantLocalFromAdjusted = instantAdjusted + offsetAdjusted;
        int offsetLocalFromAdjusted = getOffset(instantLocalFromAdjusted);
        
        // Check if still inconsistent it means we are at the edge
        if (offsetLocalFromAdjusted != offsetAdjusted) {
            long nextTransition = nextTransition(instantAdjusted);
            long nextAdjustedInstant = nextTransition(instantLocal - offsetAdjusted);

            // Look at the difference between the two transitions, and return the one that ties to the local time
            if (nextTransition != nextAdjustedInstant) {
                // Check which transition is actually correct
                if (isTransitionConflict(instantLocal, offsetLocal, offsetAdjusted)) {
                    return offsetLocal;
                }
                return offsetAdjusted;
            }
        }
    }
    // Resolve situation when the local time is during an overlap at the end of DST
    else if (offsetLocal > 0) {
        long previousTransition = previousTransition(instantAdjusted);
        if (instantAdjusted > previousTransition && instantAdjusted - previousTransition < offsetLocal) {
            return getOffset(previousTransition);
        }
    }

    return offsetAdjusted;
}

// Helper to resolve transition conflicts
protected boolean isTransitionConflict(long instantLocal, int offsetLocal, int offsetAdjusted) {
    long adjusted = instantLocal - offsetLocal;
    long adjustedNextTransition = nextTransition(adjusted);
    long adjustedPrevTransition = previousTransition(adjusted);
    return (adjustedNextTransition != nextTransition(instantLocal - offsetAdjusted) 
            || adjustedPrevTransition != previousTransition(instantLocal - offsetLocal));
}
