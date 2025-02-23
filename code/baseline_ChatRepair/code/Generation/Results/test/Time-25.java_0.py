public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalc the offset
    long instantAdjusted = instantLocal - offsetLocal;
    int offsetAdjusted = getOffset(instantAdjusted);
    
    // if the offsets differ, we must be near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // calculate difference between adjusted and local offsets
        int difference = offsetLocal - offsetAdjusted;

        // Check if transition occurs at or after the local time
        long nextTransition = nextTransition(instantAdjusted);
        if (difference > 0 && nextTransition == instantAdjusted) {
            // Adjusted next transition suggests the instantaneous was exactly at transition, use local offset
            return offsetLocal;  
        } else if (difference < 0) {
            // Check if transition occurs before the local time 
            if (nextTransition < instantLocal) {
                return offsetAdjusted;
            } else {
                return offsetLocal;
            }
        }
    }
    
    return offsetAdjusted;
}
