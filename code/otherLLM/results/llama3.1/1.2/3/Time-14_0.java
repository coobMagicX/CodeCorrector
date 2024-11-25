public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }
    // month is largest field and being added to, such as month-day
    if (DateTimeUtils.isContiguous(partial)) {
        long instant = iChronology.getMillis(values);
        if (instant >= 0 && instant < iChronology.getMaxMillis()) { // Check if date is February 29th
            int month = values[0];
            boolean isLeapYear = iChronology.isLeapYear(iChronology.getYear());
            int daysInFebruary = isLeapYear ? 29 : 28;
            
            // Handle negative month additions for dates on February 29th
            if (valueToAdd < 0) {
                while (month + valueToAdd < 1 || (month + valueToAdd == 1 && !isLeapYear)) {
                    month += valueToAdd;
                    if (!isLeapYear && month == 2) {
                        daysInFebruary = 28;
                    }
                }
            } else {
                while (month + valueToAdd > iMax) {
                    month += valueToAdd;
                    if (month - iMax >= 12 && iChronology.isLeapYear(iChronology.getYear())) {
                        daysInFebruary = 29;
                    }
                }
            }
            
            instant = iChronology.getMillis(new int[] { month, values[1] });
        } else {
            instant = add(instant, valueToAdd);
        }
        
        return iChronology.get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}