public int getOffsetFromLocal(long instantLocal) {
    // Get offset using the local instant (as if it were the resultant instant post adjustment)
    int offsetLocal = getOffset(instantLocal);

    // Adjust local instant by subtracting the first offset estimate and get the new offset
    long instantAdjusted = instantLocal - offsetLocal;
    int offsetAdjusted = getOffset(instantAdjusted);

    // Determine if local timestamp is affected by a DST gap or overlap
    if (offsetLocal != offsetAdjusted) {
        // Check which direction the adjustment went
        if (offsetLocal > offsetAdjusted) {
            // Local offset is greater (fall back scenario, clock shifted backwards)
            long nextLocal = nextTransition(instantAdjusted);
            long nextAdjusted = nextTransition(instantAdjusted - offsetAdjusted);
            
            if (nextLocal != nextAdjusted) {
                // If two different next transition points adjust to earlier offset to cope with fallback
                return offsetLocal;
            } else {
                // Adjust to later time to cope with fallback correctly, moving out of DST
                return offsetAdjusted;
            }
        } else {
            // Spring forward, clock shifts forward, lose hour
            long prevAdjusted = previousTransition(instantAdjusted);
            // A previous transition based on the adjusted instant can indicate a new offset may apply
            // only use adjusted if prev adjusted transition falls before current local adjusted
            if (prevAdjusted < instantAdjusted) {
                return offsetAdjusted;
            } else {
                return offsetLocal;
            }
        }
    }

    return offsetAdjusted; // Standard case where no boundary effects are in play
}
