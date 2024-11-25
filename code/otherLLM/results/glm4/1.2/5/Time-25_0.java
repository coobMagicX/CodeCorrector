public int getOffsetFromLocal(long instantLocal) {
    // Get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // Adjust instantLocal using the estimate and recalculate the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);
    
    // If the offsets differ, we must be near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // We need to ensure that time is always after the DST gap
        // This happens naturally for positive offsets, but not for negative
        if ((offsetLocal - offsetAdjusted) < 0) {
            // If we just return offsetAdjusted, then the time is pushed back before the transition,
            // whereas it should be on or after the transition. To address this, we adjust the instant to
            // be right after the transition.
            long nextLocal = nextTransition(instantAdjusted);
            if (nextLocal != Long.MIN_VALUE) { // Ensure there's a valid transition
                return offsetAt(nextLocal);
            }
        } else {
            // If the difference is positive, it means we need to adjust the time to be before the transition.
            long previousLocal = previousTransition(instantAdjusted);
            if (previousLocal != Long.MIN_VALUE) { // Ensure there's a valid transition
                return offsetAt(previousLocal);
            }
        }
    }
    
    return offsetAdjusted;
}

// These helper methods would need to be defined in order for the above code to work:
/*
public int getOffset(long instant) {
    // Method implementation for getting offset.
}

public long nextTransition(long instant) {
    // Method implementation for finding the next transition after a given instant.
}

public long previousTransition(long instant) {
    // Method implementation for finding the previous transition before a given instant.
}

public int offsetAt(long instant) {
    // Method implementation to get the offset at a specific instant.
}
*/