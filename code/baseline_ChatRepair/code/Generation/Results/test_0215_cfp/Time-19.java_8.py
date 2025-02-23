public int getOffsetFromLocal(long instantLocal) {
    // First attempt to find the offset based on local time
    int offset1 = getOffset(instantLocal);
    
    // Calculate the offset from what is effectively the standard time from that local time
    int standardOffset = getStandardOffset(instantLocal - offset1);
    
    // Calculate actual Instant from UTC that corresponds to attempt using the initial guessed offset
    long millisAdjusted = instantLocal - offset1;
    
    // Now find the offset that actually corresponds to this adjusted instant
    int offset2 = getOffset(millisAdjusted);
    
    // If the two offsets agree, we're not in a problematic area and can return either
    if (offset1 == offset2) {
        return offset1;
    }
    
    // Otherwise, we need to determine which offset is correct around this transition point
    // Essentially, choose offset that would be used just before instantLocal
    long millisAdjustedEarlier = instantLocal - offset2;
    long millisDelta = millisAdjusted - millisAdjustedEarlier;
    
    // Get the next transition that occurs strictly after the first adjusted time
    long nextTransition = nextTransition(millisAdjustedEarlier);
    
    // Check if the current adjusted time is closer to the transition or not
    if (nextTransition == Long.MAX_VALUE || (millisAdjustedEarlier <= nextTransition && nextTransition < millisAdjusted)) {
        // If there is no next transition or the next transition happens strictly after the earlier adjusted
        // and before current adjusted time, use the later offset
        return offset2;
    }
    
    // Otherwise, use the earlier offset
    return offset1;
}
