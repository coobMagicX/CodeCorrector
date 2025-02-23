public int getOffsetFromLocal(long instantLocal) {
    // first estimate of the offset
    int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalculate the offset
    long instantAdjusted = instantLocal - offsetLocal;
    int offsetAdjusted = getOffset(instantAdjusted);

    // if the offsets differ, we must be near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // Handle situation around DST boundary
        long nextTransition = nextTransition(instantAdjusted);
        long difference = nextTransition - instantAdjusted;

        // Check which offset is valid for the given local instant
        if (difference < 0 && offsetLocal > offsetAdjusted) {
            return offsetLocal;
        } else if (difference > 0 && offsetLocal < offsetAdjusted) {
            // ensure on or after the transition
            long nextLocal = nextTransition(instantAdjusted);
            if (instantLocal >= nextLocal) {
                return offsetAdjusted;
            } else {
                return offsetLocal;
            }
        }
    }
    
    // return the adjusted offsets
    return offsetAdjusted;
}
