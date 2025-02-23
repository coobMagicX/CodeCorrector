public int getOffsetFromLocal(long instantLocal) {
    int offsetLocal = getOffset(instantLocal);

    // The instant adjusted by the original offset might be inaccurate around DST boundaries
    long instantAdjusted = instantLocal - offsetLocal;
    int offsetAdjusted = getOffset(instantAdjusted);

    // If both offsets are the same, we can safely return any of them
    if (offsetLocal == offsetAdjusted) {
        return offsetLocal;
    }
    
    // Calculate difference which shows if we cross a DST boundary
    long difference = offsetAdjusted - offsetLocal;

    // Recheck by adjusting to the modified offset and finding yet another offset
    long instantDoubleAdjusted = instantLocal - offsetAdjusted;
    int offsetDoubleAdjusted = getOffset(instantDoubleAdjusted);

    // Compare all three offsets, which helps near larger DST transitions.
    if (offsetAdjusted != offsetDoubleAdjusted) {
        // Chains of difference imply a recurring shift in DST boundaries; choose one that matches.
        return Math.min(offsetAdjusted, offsetDoubleAdjusted);
    }

    // When the first adjustment gives the correct offset, just use it
    if (difference > 0) {
        // Positive difference means we're in or past a transition from low to high (DST started)
        // Adjusted time is ahead in the high offset zone
        return offsetAdjusted;
    } else {
        // Negative difference means we're in a transition from high to low (DST ended)
        // Time with earlier offset applies, reflect to a moment just before transition
        return offsetLocal;
    }
}
