public long adjustOffset(long instant, boolean earlierOrLater) {
    // Work out range of instants that have duplicate local times
    long local = convertUTCToLocal(instant);
    
    int initialOffset = getOffset(local);
    boolean isOverlap = false;
    boolean hasGap = false;

    // Check if there's a gap before the overlap or during the overlap
    long instantBeforeOverlap = local - initialOffset;
    long prevTransitionInstant = previousTransition(instantBeforeOverlap);
    if (prevTransitionInstant != -1 && prevTransitionInstant >= instantBeforeOverlap) {
        hasGap = isLocalDateTimeGap(new LocalDateTime(prevTransitionInstant));
    }

    // Check if there's an overlap after the current instant
    long nextTransitionInstant = nextTransition(local);
    if (nextTransitionInstant != -1 && nextTransitionInstant <= local) {
        isOverlap = true;
    }

    int newOffset;
    if (hasGap || isOverlap) {
        newOffset = getOffsetFromLocal(local);
    } else {
        newOffset = initialOffset;
    }

    // Calculate the adjusted instant based on whether we should go earlier or later
    long adjustedInstant = local + (earlierOrLater ? -initialOffset : 0) + (newOffset - initialOffset);

    return convertLocalToUTC(local, false, adjustedInstant);
}