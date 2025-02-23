public int getOffsetFromLocal(long instantLocal) {
    // Initial estimate of the offset from the local timestamp
    final int offsetInitial = getOffset(instantLocal);
    
    // Adjusting the instant with our initial offset estimate
    final long instantAdjusted = instantLocal - offsetInitial;
    // Fetch the definitive offset for the newly adjusted timestamp
    final int offsetAdjusted = getOffset(instantAdjusted);
    
    // Check for a change in the offset which indicates crossing a DST boundary
    if (offsetInitial != offsetAdjusted) {
        // Find the next transition point after the adjusted instant
        long nextTransitionAfterInitial = nextTransition(instantAdjusted);

        // Recalculate offset using the normal local time with the newly calculated offset
        final long instantAdjustedAgain = instantLocal - offsetAdjusted;
        
        // Obtain the next transition time based on this new instant
        long nextTransitionAfterAdjusted = nextTransition(instantAdjustedAgain);
        
        // Check if these transitions are different, which indicates a DST gap issue
        if (nextTransitionAfterInitial != nextTransitionAfterAdjusted) {
            // If the transitions don't match, use the initial offset to place the local time
            // correctly relative to the original estimate instead of pushing back over a transition
            return offsetInitial;
        }
    }
    
    // Return the definitive adjusted offset if there was no transition issue
    return offsetAdjusted;
}
