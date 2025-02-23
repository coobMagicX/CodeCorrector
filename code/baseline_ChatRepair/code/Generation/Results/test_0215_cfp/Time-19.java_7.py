public int getOffsetFromLocal(long instantLocal) {
    // Initial offset estimate at the given local instant
    final int initialOffset = getOffset(instantLocal);
    // Adjust instant using the initial offset
    final long adjustedInstant = instantLocal - initialOffset;
    // Potential new offset at adjusted instant
    final int newOffset = getOffset(adjustedInstant);
    
    // Check if initial and new offsets differ
    if (initialOffset != newOffset) {
        // Calculate next transition using new offset
        long nextTransition = nextTransition(adjustedInstant);
        // Calculate offset at the original local time minus new offset (checks validity under new offset)
        final int transitionalOffset = getOffset(instantLocal - newOffset);

        // Determine which offset to use by evaluating which transition and offset correctly align with local time
        if (transitionalOffset == newOffset) {
            return transitionalOffset;
        } else if (nextTransition == Long.MAX_VALUE || instantLocal <= (nextTransition - (newOffset - initialOffset))) {
            // If the instant is before the next transition corrected for the offset difference, or no transition exists
            return initialOffset;
        } else {
            return newOffset;
        }
    }

    // If the new offset equals the initial estimate, ensure that the adjusted instant is valid and if so, use the offset
    long previousTransition = previousTransition(adjustedInstant);
    if (previousTransition < adjustedInstant) {
        // If adjusted instant is after the previous transition, ensure it aligns with the determined offsets
        int previousOffset = getOffset(previousTransition);
        if (adjustedInstant - previousTransition < initialOffset - previousOffset) {
            return previousOffset;
        }
    }
    return newOffset;
}
