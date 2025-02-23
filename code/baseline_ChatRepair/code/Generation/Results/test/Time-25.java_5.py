public int getOffsetFromLocal(long instantLocal) {
    // Get the offset for the local time (without any adjustments)
    int offsetLocal = getOffset(instantLocal);
    
    // Calculate an 'adjusted' instant using this offset
    long instantAdjusted = instantLocal + offsetLocal;
    
    // Get the offset for the adjusted instant (this may differ if we crossed a DST boundary)
    int offsetAdjusted = getOffset(instantAdjusted);
    
    // Normally, you would use the adjusted offset, but if they differ check more carefully
    if (offsetLocal != offsetAdjusted) {
        // Recheck the offset using the adjusted offset, this helps correct any discrepancies from the first adjustment
        long instantRechecked = instantLocal - offsetAdjusted;
        int offsetRechecked = getOffset(instantRechecked);
        
        // Check overlap. If there's a jump in local time caused by DST transition, we may have two or more offsets applicable.
        if (offsetRechecked == offsetAdjusted) {
            return offsetAdjusted;
        } else {
            // Calculate possible next transitions to rule out ambiguities around DST boundaries
            long nextTransitionLocal = nextTransition(instantLocal - offsetLocal);
            long nextTransitionAdjusted = nextTransition(instantAdjusted);
            
            // Use offset that points to latest valid transition (greater than instantLocal)
            if (nextTransitionAdjusted > nextTransitionLocal) {
                return offsetLocal;
            } else {
                return offsetAdjusted;
            }
        }
    }

    // Use the adjusted as it confirmed no ambiguity or transition issues
    return offsetAdjusted;
}
