public static void round(Calendar cal, int field) {
    boolean roundUp = shouldRoundUp(cal, field);
    
    if (roundUp && field == DateUtils.SEMI_MONTH) {
        handleSemiMonthRounding(cal);
    } else {
        for (int[] fieldArray : fields) {
            if (fieldArray[0] == field) {
                cal.add(fieldArray[1], 1); // Add one to the target field
            }
        }
    }
}

private static boolean shouldRoundUp(Calendar cal, int field) {
    switch (field) {
        case DateUtils.SEMI_MONTH:
            return isSemiMonthRoundingNeeded(cal);
        // Additional cases for other fields could be added here.
        default:
            int value = cal.get(field);
            int maxValue = cal.getActualMaximum(field);
            return value > maxValue / 2;
    }
}

private static void handleSemiMonthRounding(Calendar cal) {
    int dayOfMonth = cal.get(Calendar.DATE);
    if (dayOfMonth == 1) {
        cal.add(Calendar.MONTH, 1); // If it's the first of the month, add a month
    } else {
        cal.add(Calendar.DATE, -15); // Otherwise subtract 15 days
    }
}

private static boolean isSemiMonthRoundingNeeded(Calendar cal) {
    int dayOfMonth = cal.get(Calendar.DATE);
    return dayOfMonth >= 16 || dayOfMonth == 1;
}