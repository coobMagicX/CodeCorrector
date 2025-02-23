public int getOffsetFromLocal(long instantLocal) {
    // Get the offset at instantLocal (initial estimate)
    int initialOffset = getOffset(instantLocal);
    // Adjust instantLocal using the initial estimate and recalculate the offset
    long adjustedInstant = instantLocal - initialOffset;
    int adjustedOffset = getOffset(adjustedInstant);

    // Check if the initial and adjusted offsets are different
    if (adjustedOffset != initialOffset) {
        // Determine if we are dealing with a DST overlap
        boolean inOverlap = isLocalTimeOverlap(instantLocal, initialOffset, adjustedOffset);

        if (inOverlap) {
            // Return the later offset to ensure we remain after the DST transition
            // Assuming the negative offset indicates the standard (winter) time which comes after DST ends.
            return Math.max(initialOffset, adjustedOffset);
        } else {
            // In case of a gap due to DST where clocks skip forward
            // Example: Clocks move from 01:59 to 03:00
            if ((initialOffset - adjustedOffset) < 0) {
                // Check if the adjusted instant should belong to the next valid offset
                long nextValidInstant = nextTransition(adjustedInstant);
                if (instantLocal < nextValidInstant - adjustedOffset) {
                    return initialOffset; // still within the range of the earlier offset
                }
            }
        }
    }

    return adjustedOffset;
}

// Helper method to check if the local time has an overlap due to DST offset changes
private boolean isLocalTimeOverlap(long instantLocal, int initialOffset, int adjustedOffset) {
    long overlapStart = nextTransition(instantLocal - initialOffset);
    long overlapEnd = nextTransition(instantLocal - adjustedOffset);

    // If these transitions are not the same, it indicates an overlap
    return overlapStart != overlapEnd;
}
