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
    long instantLocal = LocalDateTime.of(2022, 10, 30, 1, 0, 0).toEpochSecond(ZoneOffset.UTC);
    int expectedOffset = 3 * 60 * 60; // Moscow is UTC+3 during DST
    int actualOffset = getOffsetFromLocal(instantLocal);
    assertEquals(expectedOffset, actualOffset);
    
    // Test for each minute within an hour during DST transition in Moscow in autumn
    for (int min = 0; min < 60; min++) {
        instantLocal = LocalDateTime.of(2022, 10, 30, 2, min, 0).toEpochSecond(ZoneOffset.UTC);
        actualOffset = getOffsetFromLocal(instantLocal);
        assertEquals(expectedOffset, actualOffset);
    }
}