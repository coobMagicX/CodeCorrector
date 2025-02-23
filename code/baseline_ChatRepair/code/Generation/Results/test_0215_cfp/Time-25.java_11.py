public int getOffsetFromLocal(long instantLocal) {
    int offsetLocal = getOffset(instantLocal);
    long instantAdjusted = instantLocal - offsetLocal;
    int offsetAdjusted = getOffset(instantAdjusted);

    // Check if adjusting by the local offset would move the timestamp across a DST transition
    if (offsetLocal != offsetAdjusted) {
        // Determine the transition point using the adjusted offset
        long transitionMillis = nextTransition(instantAdjusted);
        
        // Check if the original local timestamp falls within the period handled by offsetAdjusted
        if (instantLocal >= transitionMillis) {
            // After the transition, use offsetAdjusted
            return offsetAdjusted;
        } else {
            // Before the transition, use offsetLocal
            return offsetLocal;
        }
    }
    return offsetLocal;
}
