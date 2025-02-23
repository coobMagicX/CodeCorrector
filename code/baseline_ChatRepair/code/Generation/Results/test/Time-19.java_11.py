public int getOffsetFromLocal(long instantLocal) {
    // Get offset based on just the local timestamp (initial assumption)
    int offsetLocal = getOffset(instantLocal);
    
    // Adjust the timestamp back using the initial offset
    long instantAdjusted = instantLocal - offsetLocal;

    // Get the offset at the adjusted timestamp
    int offsetAdjusted = getOffset(instantAdjusted);

    // If offsets are the same, return either as they are effectively equal
    if (offsetLocal == offsetAdjusted) {
        return offsetLocal;
    }

    // Transition detection: Calculate the 'next transition' if there's a mismatch to determine overlap
    long nextTransition = nextTransition(instantAdjusted);

    // Determine overlap. During fall back, local time repeats. Choose the latest offset (i.e., the positive one) within the overlap.
    if (instantLocal >= nextTransition - offsetAdjusted && instantLocal < nextTransition - offsetLocal) {
        return Math.max(offsetLocal, offsetAdjusted);
    }

    // Default to the adjusted offset when there's no overlap
    return offsetAdjusted;
}
