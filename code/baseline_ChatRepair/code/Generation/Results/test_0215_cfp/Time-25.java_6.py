public int getOffsetFromLocal(long instantLocal) {
    // Estimate the offset at instantLocal
    int offsetLocal = getOffset(instantLocal);
    // Adjust instantLocal using the estimate and recalculate the offset
    long instantAdjusted = instantLocal - offsetLocal;
    int offsetAdjusted = getOffset(instantAdjusted);

    // If the offsets differ, we may be near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // Determine if the new instant is after the transition
        boolean afterTransition = true;
        if (offsetLocal > offsetAdjusted) {
            afterTransition = (instantLocal - (long) offsetLocal) >= nextTransition(instantAdjusted);
        } else {
            afterTransition = (instantLocal - (long) offsetAdjusted) >= nextTransition(instantAdjusted);
        }

        // Make further adjustments if the new instant is not after the transition
        if (!afterTransition) {
            // Check which offset keeps the instant after the transition
            long prevTransition = previousTransition(instantAdjusted);
            if ((instantLocal - offsetLocal) < prevTransition && (instantLocal - offsetAdjusted) >= prevTransition){
                return offsetAdjusted;
            } else {
                return offsetLocal;
            }
        }
    }
    return offsetAdjusted;
}
