public int getOffsetFromLocal(long instantLocal) {
    int originalOffset = getOffset(instantLocal);
    // Attempt to adjust the instant using the original offset
    long adjustedInstant = instantLocal - originalOffset;
    int adjustedOffset = getOffset(adjustedInstant);

    // Check if the adjusted offset disagrees with the original offset
    if (originalOffset != adjustedOffset) {
        // Adjust backwards with the adjusted offset (this is crucial for fall-back transitions)
        long oppositeAdjustedInstant = instantLocal - adjustedOffset;
        int oppositeAdjustedOffset = getOffset(oppositeAdjustedInstant);

        if (adjustedOffset == oppositeAdjustedOffset) {
            // Both adjustments agree after the change; prefer the new offset if it agrees after double adjustment
            return adjustedOffset;
        } else if (originalOffset == oppositeAdjustedOffset) {
            // Both adjustments disagree and reverting to original if it agrees with the double backward adjustment
            // This case can happen if there's an overlap in the offsets and we're at a boundary.
            return originalOffset;
        }
    } 

    // Generally return the adjusted offset in other cases
    return adjustedOffset;
}
