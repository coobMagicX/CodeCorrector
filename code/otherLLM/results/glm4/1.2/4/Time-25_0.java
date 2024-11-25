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
            // if we just return offsetAdjusted then the time is pushed back before the transition,
            // whereas it should be on or after the transition.
            long nextLocal = nextTransition(instantAdjusted);
            long prevAdjusted = previousTransition(instantAdjusted);

            // Check if instantAdjusted is before the DST gap and between the previous and next transitions
            if (prevAdjusted <= instantAdjusted && instantAdjusted < nextLocal) {
                return offsetLocal;
            }
        }
    }
    return offsetAdjusted;
}

// Method to get the previous transition time
private long previousTransition(long instant) {
    // This method should return the previous DST transition time for the given instant
    // For example, using ZonedDateTime and zone rules:
    ZoneId zone = ZoneId.of("Europe/Moscow");
    ZonedDateTime now = ZonedDateTime.ofInstant(Instant.ofEpochSecond(instant), zone);
    return now.toInstant().toEpochMilli();
}

// Method to get the next transition time
private long nextTransition(long instant) {
    // This method should return the next DST transition time for the given instant
    // For example, using ZonedDateTime and zone rules:
    ZoneId zone = ZoneId.of("Europe/Moscow");
    ZonedDateTime now = ZonedDateTime.ofInstant(Instant.ofEpochSecond(instant), zone);
    return now.toInstant().toEpochMilli();
}