public static DateTimeZone forOffsetHoursMinutes(int hoursOffset, int minutesOffset) throws IllegalArgumentException {
    if (hoursOffset == 0 && minutesOffset == 0) {
        return DateTimeZone.UTC;
    }
    if (hoursOffset < -23 || hoursOffset > 23) {
        throw new IllegalArgumentException("Hours out of range: " + hoursOffset);
    }
    if (minutesOffset < 0 || minutesOffset > 59) {
        throw new IllegalArgumentException("Minutes out of range: " + minutesOffset);
    }
    int offset = 0;
    try {
        int hoursInMinutes = hoursOffset * 60;
        if (hoursInMinutes < 0) {
            minutesOffset = hoursInMinutes - minutesOffset;
        } else {
            minutesOffset = hoursInMinutes + minutesOffset;
        }
        offset = FieldUtils.safeMultiply(minutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    return forOffsetMillis(offset);
}

public static DateTimeZone forOffsetMillis(int offset) {
    long instant = System.currentTimeMillis();
    boolean earlierOrLater = false;
    
    // a bit messy, but will work in all non-pathological cases
    long instantBefore = instant - 3 * DateTimeConstants.MILLIS_PER_HOUR;
    long instantAfter = instant + 3 * DateTimeConstants.MILLIS_PER_HOUR;
    long offsetBefore = getOffset(instantBefore);
    long offsetAfter = getOffset(instantAfter);
    if (offsetBefore <= offsetAfter) {
        return DateTimeZone.forOffsetMillis(offset);  // not an overlap (less than is a gap, equal is normal case)
    }
    
    // work out range of instants that have duplicate local times
    long diff = offsetBefore - offsetAfter;
    long transition = nextTransition(instantBefore);
    long overlapStart = transition - diff;
    long overlapEnd = transition + diff;
    if (instant < overlapStart || instant >= overlapEnd) {
        return DateTimeZone.forOffsetMillis(offset);  // not an overlap
    }
    
    // calculate result
    long afterStart = instant - overlapStart;
    if (afterStart >= diff) {
        // currently in later offset
        return DateTimeZone.forOffsetMillis(offset + diff);
    } else {
        // currently in earlier offset
        return DateTimeZone.forOffsetMillis(offset);
    }
}

public static long getOffset(long instant) {
    // implementation of getOffset method
}

public static long nextTransition(long instant) {
    // implementation of nextTransition method
}

public static class DateTimeZone {
    public static final DateTimeZone UTC = new DateTimeZone("UTC");
    
    private String id;
    
    public DateTimeZone(String id) {
        this.id = id;
    }
    
    public static DateTimeZone forOffsetMillis(int offset) {
        // implementation of forOffsetMillis method
    }
}