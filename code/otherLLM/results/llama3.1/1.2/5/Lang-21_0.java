public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    int year = getFragmentInDays(cal1, Calendar.YEAR);
    int month = getFragmentInDays(cal1, Calendar.MONTH);
    int day = getFragmentInDays(cal1, Calendar.DAY_OF_YEAR);
    int era = getFragmentInDays(cal1, Calendar.ERA);

    return (cal1.get(Calendar.HOUR) == cal2.get(Calendar.HOUR) &&
            cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE) &&
            cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND) &&
            cal1.get(Calendar.MILLISECOND) == cal2.get(Calendar.MILLISECOND));
}