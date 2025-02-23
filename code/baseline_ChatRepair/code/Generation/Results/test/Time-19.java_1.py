public int getOffsetFromLocal(long instantLocal) {
    int offsetLocal = getOffset(instantLocal);
    long instantAdjusted = instantLocal - offsetLocal;
    int offsetAdjusted = getOffset(instantAdjusted);

    if (offsetLocal != offsetAdjusted) {
        // Calculate difference and which way adjustment shifted
        long difference = instantAdjusted - (instantLocal - offsetAdjusted);
        boolean earlierOffset = offsetAdjusted < offsetLocal;

        // Adjust next and previous transitions based on adjusted offset
        long nextTransition = nextTransition(instantAdjusted);
        int nextTransitionOffset = earlierOffset ? getOffset(nextTransition - 1) : offsetAdjusted;
        long previousTransition = previousTransition(instantAdjusted);
        int previousTransitionOffset = earlierOffset ? offsetAdjusted : getOffset(previousTransition - 1);

        // Normalize the transitions using the calculated offsets
        nextTransition -= nextTransitionOffset;
        previousTransition -= previousTransitionOffset;

        // Check the surrounding transitions
        if (earlierOffset && instantAdjusted < nextTransition) {
            return earlierOffset ? previousTransitionOffset : nextTransitionOffset;
        } else if (!earlierOffset && instantAdjusted >= previousTransition) {
            return earlierOffset ? nextTransitionOffset : previousTransitionOffset;
        }
    }
    return offsetAdjusted;
}
