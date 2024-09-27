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
            long nextLocal = nextTransition(instantAdjusted);
            long nextAdjusted = nextTransition(instantLocal - offsetAdjusted);
            if (nextLocal != nextAdjusted) {
                return offsetLocal;
            }
        }
    }
    return offsetAdjusted;
}

public void doTest_getOffsetFromLocal() {
    // Test for DST transition in Moscow in autumn
    // Start time: October 26th, 2019, 01:00 AM UTC+3
    // End time: October 26th, 2019, 03:00 AM UTC+3 (DST ends, clocks are set back 1 hour)
    // Test each minute within this hour
    for (int min = 0; min < 60; min++) {
        String timeString = "2019-10-26T01:" + String.format("%02d", min) + ":00";
        long instantLocal = LocalDateTime.parse(timeString).atZone(ZoneId.of("Europe/Moscow")).toInstant().toEpochMilli();
        int offsetFromLocal = getOffsetFromLocal(instantLocal);
        // The offset should be 3 (UTC+3) for the whole hour
        assertEquals(3 * 3600000, offsetFromLocal);
    }
}