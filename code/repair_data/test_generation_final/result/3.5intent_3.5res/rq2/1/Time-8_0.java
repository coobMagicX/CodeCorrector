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

public static DateTimeZone forOffsetMillis(int offsetMillis) throws IllegalArgumentException {
    String id = printOffset(offsetMillis);
    return fixedOffsetZone(id, offsetMillis);
}

private static DateTimeZone fixedOffsetZone(String id, int offsetMillis) {
    if (offsetMillis == 0) {
        return DateTimeZone.UTC;
    }
    return new FixedDateTimeZone(id, null, offsetMillis, offsetMillis);
}

private static String printOffset(int offsetMillis) {
    if (offsetMillis == 0) {
        return "UTC";
    }
    StringBuilder buf = new StringBuilder();
    if (offsetMillis >= 0) {
        buf.append('+');
    } else {
        buf.append('-');
        offsetMillis = -offsetMillis;
    }
    int hours = offsetMillis / DateTimeConstants.MILLIS_PER_HOUR;
    FormatUtils.appendPaddedInteger(buf, hours, 2);
    offsetMillis -= hours * DateTimeConstants.MILLIS_PER_HOUR;
    int minutes = offsetMillis / DateTimeConstants.MILLIS_PER_MINUTE;
    buf.append(':');
    FormatUtils.appendPaddedInteger(buf, minutes, 2);
    return buf.toString();
}