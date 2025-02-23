public int getOffsetFromLocal(long instantLocal) {
    // First estimate of the offset based on the local timestamp
    int offsetLocal = getOffset(instantLocal);
    // Adjust the local timestamp by subtracting the first estimate
    long instantAdjusted = instantLocal - offsetLocal;
    // Get the offset for the adjusted timestamp
    int offsetAdjusted = getOffset(instantAdjusted);

    // If the two offset estimates are different, we might be around a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // Calculate the transition point using the adjusted timestamp
        long transition = nextTransition(instantAdjusted);
        
        // Check impact of the transition point to select appropriate offset
        // If the local timestamp adjusted by the initially calculated offset falls into a gap,
        // this is possibly due to backward transition, hence the adjusted timestamp 
        // (which falls we hope into a valid range) should provide the more proper offset.
        if (instantLocal < transition) {
            return offsetLocal;
        } else {
            long transition2 = nextTransition(instantLocal - offsetAdjusted);
            if (transition2 != transition) {
                return offsetLocal; 
            }
        }
    }

    // Return the adjusted offset if we find no contradictions or specific cases
    return offsetAdjusted;
}
