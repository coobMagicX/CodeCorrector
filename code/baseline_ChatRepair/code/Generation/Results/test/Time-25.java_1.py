public int getOffsetFromLocal(long instantLocal) {
    // First guess of the offset, based purely on the local timestamp
    int offsetLocal = getOffset(instantLocal);

    // Calculate the instant that would result from applying the first guess offset
    long instantAdjusted = instantLocal - offsetLocal;

    // Get the offset again, this time based on the adjusted instant
    int offsetAdjusted = getOffset(instantAdjusted);

    // If the first guess offset differs from the adjusted offset, a DST boundary might be involved
    if (offsetLocal != offsetAdjusted) {
        // Get the actual transition time given the adjusted instant
        long nextTransition = nextTransition(instantAdjusted);

        // The transition calculation is corrected by considering whether the local instant should stay within the next transition
        if (instantAdjusted >= nextTransition) {
            // Using the adjusted offset as we are after the transition
            return offsetAdjusted;
        } else {
            // Using the local offset as we are before the transition
            return offsetLocal;
        }
    }

    // Use adjusted offset if both are the same
    return offsetAdjusted;
}
