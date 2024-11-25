public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The date must not be null");
    }

    // Correct the comparison logic to account for different time zones by using UTC values.
    long utcMillis1 = cal1.getTimeInMillis() - cal1.getZoneOffset();
    long utcMillis2 = cal2.getTimeInMillis() - cal2.getZoneOffset();

    return (cal1.getClass() == cal2.getClass()) && (utcMillis1 == utcMillis2);
}