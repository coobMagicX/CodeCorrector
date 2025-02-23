public int getOffsetFromLocal(long instantLocal) {
    // First estimate of offset based on local timestamp
    int offsetLocal = getOffset(instantLocal);
    
    // Adjust the instant by subtracting the estimated offset
    long instantAdjusted = instantLocal - offsetLocal;
    int offsetAdjusted = getOffset(instantAdjusted);
    
    // If the offsets differ, we are likely at a transition point
    if (offsetLocal != offsetAdjusted) {
        // Determine whether we're dealing with a forward or backward transition
        if (offsetLocal > offsetAdjusted) {
            // This might mean we're falling back, so consider the transition instant
            long prevTransition = previousTransition(instantAdjusted);
            // If moving from DST to standard time, and local time is before transition
            if (instantLocal < instantAdjusted && prevTransition <= instantLocal) {
                return offsetLocal;
            } else {
                return offsetAdjusted;
            }
        } else {
            // Moving forward (spring forward)
            long nextTransition = nextTransition(instantAdjusted);
            // Scenario where local time is beyond the transition, consider previous estimate
            if (instantLocal > instantAdjusted && nextTransition > instantLocal) {
                return offsetLocal;
            } else {
                return offsetAdjusted;
            }
        }
    } else {
        // No differences means no transition overlap, return adjusted offset
        return offsetAdjusted;
    }
}
