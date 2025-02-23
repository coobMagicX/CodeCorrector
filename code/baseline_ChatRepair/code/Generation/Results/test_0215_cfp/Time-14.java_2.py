public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // scenarios like 2004-02-29 + 48 months -> 2008-02-29, which should roll over correctly
    if (valueToAdd == 0) {
        return values;
    }

    int monthIndex = -1;
    int dayIndex = -1;

    // Find the indices of month and day fields
    for (int i = 0; i < partial.size(); i++) {
        DateTimeFieldType type = partial.getFieldType(i);
        if (type == DateTimeFieldType.monthOfYear()) {
            monthIndex = i;
        } else if (type == DateTimeFieldType.dayOfMonth()) {
            dayIndex = i;
        }
    }

    if (monthIndex >= 0) {
        // Adjust the month and handle rolling over years if needed
        values[monthIndex] += valueToAdd;

        // Periodic adjustment of the months, catering for overflow (e.g., from December to January)
        int maxMonth = 12;
        int yearsToAdd = (values[monthIndex] - 1) / maxMonth; // Calculate full years to add
        values[monthIndex] = (values[monthIndex] - 1) % maxMonth + 1; // Calculate remaining months in the year range 1 to 12

        if (partial.isSupported(DateTimeFieldType.year()) && yearsToAdd != 0) {
            int yearIndex = -1;
            for (int i = 0; i < partial.size(); i++) {
                if (partial.getFieldType(i) == DateTimeFieldType.year()) {
                    yearIndex = i;
                    break;
                }
            }
            values[yearIndex] += yearsToAdd;
        }

        if (dayIndex >= 0) {
            // Recalculate the given date with newly modified values:before changing the day to maximum available for month
            long millis = 0L;
            for (int i = 0; i < partial.size(); i++) {
                millis = partial.getFieldType(i).getField(iChronology).set(millis, values[i]);
            }

            int maxDayOfMonth = iChronology.dayOfMonth().getMaximumValue(millis);
            if (values[dayIndex] > maxDayOfMonth) {
                values[dayIndex]