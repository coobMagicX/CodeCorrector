public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalc the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);
    
    // if the offsets differ, we must be near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // we need to ensure that time is always after the DST gap
        // this happens naturally for positive offsets, but not for negative
        if ((offsetLocal - offsetAdjusted) < 0) {
            // if we just return offsetAdjusted then the time is pushed
            // back before the transition, whereas it should be
            // on or after the transition
            
            // Corrected logic to handle negative offsets
            long nextLocal = nextTransition(instantAdjusted);
            long nextAdjusted = nextTransition(instantLocal - offsetAdjusted);
            
            // Check if the adjusted instant is still in the same DST zone as instantLocal
            if (nextLocal != nextAdjusted) {
                return getOffset(nextTransition(instantLocal));
            }
        } else {
            // If offsets are equal and positive, we can safely use the adjusted instant for further calculations
            long prev = previousTransition(instantAdjusted);
            if (prev < instantAdjusted) {
                int offsetPrev = getOffset(prev);
                int diff = offsetPrev - offsetLocal;
                if (instantAdjusted - prev <= diff) {
                    return getOffset(nextTransition(instantLocal));
                }
            } else {
                // If offsets are equal and negative, we can safely use the adjusted instant for further calculations
                long next = nextTransition(instantAdjusted);
                if (next > instantAdjusted) {
                    int offsetNext = getOffset(next);
                    int diff = offsetNext - offsetLocal;
                    if (instantAdjusted - next <= diff) {
                        return getOffset(nextTransition(instantLocal));
                    }
                } else {
                    // If offsets are equal and the adjusted instant is on a DST boundary, use the original instant
                    long prevBoundary = previousTransition(instantLocal);
                    if (prevBoundary < instantLocal) {
                        int offsetPrev = getOffset(prevBoundary);
                        return getOffset(nextTransition(instantLocal));
                    } else {
                        return getOffset(nextTransition(instantLocal));
                    }
                }
            }
        }
    } else {
        // If offsets are equal, we can safely use the adjusted instant for further calculations
        return getOffset(nextTransition(instantLocal));
    }
    
    // Return the offset from the next transition of the original instant
    return getOffset(nextTransition(instantLocal));
}