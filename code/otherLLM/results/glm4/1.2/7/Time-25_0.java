public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalc the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);
    // if the offsets differ, we must be near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // calculate the difference between the two offsets
        int offsetDifference = Math.abs(offsetLocal - offsetAdjusted);

        // Check if the instant is before or after the DST gap
        boolean isBeforeDSTGap = (offsetLocal < offsetAdjusted);

        // If we are before the DST gap, we need to adjust the time to ensure it's after the transition
        if (isBeforeDSTGap) {
            long nextTransitionInstant;
            if (offsetDifference > 0) { // There is a DST gap
                // Find the next transition after instantAdjusted
                nextTransitionInstant = nextTransition(instantAdjusted);
            } else {
                // No DST gap, so just use the original instantLocal
                nextTransitionInstant = instantLocal;
            }

            // Adjust the time to be after the DST gap
            long newInstantAfterDSTGap = adjustTimeAfterDSTGap(nextTransitionInstant);
            return getOffset(newInstantAfterDSTGap);
        }
    }
    return offsetAdjusted;
}

private long adjustTimeAfterDSTGap(long instant) {
    DateTime dateTime = new DateTime(instant * 1000L); // Convert to milliseconds
    DateTimeZone zone = DateTimeZone.getDefault();
    ZoneRules rules = zone.getRules();

    // Find the transition time after which DST has already started
    DateTime nextTransition = null;
    for (DateTimeIterator iterator = new DateTimeIterator(dateTime, ChronoField.DAY_OF_YEAR);
         !iterator.isAfter(rules.getLastRuleChange(dateTime.withDayOfYear(iterator.getValue())));
         iterator.addDays(1)) {
        DateTime ruleChange = rules.getRule(iterator.getValue()).getStart().withMillisOfDay(0L);
        if (!ruleChange.isBefore(dateTime)) {
            nextTransition = ruleChange;
            break;
        }
    }

    // If we found a transition, add the gap to it
    if (nextTransition != null) {
        return nextTransition.plusSeconds(3600).getMillis() / 1000L; // Add one hour to the transition time
    } else {
        // No DST transition found, so just return the instant unchanged
        return instant;
    }
}