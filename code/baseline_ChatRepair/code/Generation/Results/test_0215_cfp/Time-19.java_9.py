public int getOffsetFromLocal(long instantLocal) {
    // Initial estimate of the offset
    int offsetLocal = getOffset(instantLocal);
    
    // Adjust the instant to get the real offset at that adjusted instant
    long instantAdjusted = instantLocal - offsetLocal;
    int offsetAdjusted = getOffset(instantAdjusted);
    
    // If there's a mismatch, there could be an ambiguity due to a DST overlap
    if (offsetLocal != offsetAdjusted) {
        // Calculate the next transitions and compare them to handle overlaps
        long nextTransitionLocal = nextTransition(instantAdjusted);
        long nextTransitionAdjusted = nextTransition(instantLocal - offsetAdjusted);
        
        if (nextTransitionLocal != nextTransitionAdjusted) {
            // When the transition points don't match, return the local offset if the local time is within the overlap on getting set back
            if (offsetLocal > offsetAdjusted) {
                // Adjusted is in DST, local is in standard time: Choose standard time
                return offsetLocal;
            } else {
                // Both offsets are in DST, need to select correct one based on a specific rule
                long switchPoint = nextTransitionLocal - offsetLocal;
                if (instantLocal < switchPoint) {
                    return offsetLocal;
                } else {
                    return offsetAdjusted;
                }
            }
        }
    }
    
    // In case of strictly positive offsets and non-DST overlaps
    if (offsetLocal > 0 && offsetAdjusted > 0) {
        long previousTransition = previousTransition(instantAdjusted);
        if (instantAdjusted - previousTransition < 3600000) { // within 1 hour after the transition
            int offsetPrev = getOffset(previousTransition);
            if (offsetPrev != offsetLocal) {
                return offsetPrev;
            }
        }
    }

    return offsetAdjusted;
}
