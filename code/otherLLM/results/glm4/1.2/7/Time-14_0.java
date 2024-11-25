public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }
    
    // month is largest field and being added to, such as month-day
    if (DateTimeUtils.isContiguous(partial)) {
        long instant = partial.toInstant(iChronology);
        for (int i = 0; i < partial.size(); i++) {
            if (partial.getFieldType(i) == DateTimeFieldType.monthOfYear()) {
                int monthsToAdd;
                if (valueToAdd > 0) {
                    // Adding months, need to wrap around end of month
                    int currentMonth = iChronology.getMonthOfYear(instant);
                    int yearOfNextMonth = iChronology.getYear(instant);
                    monthsToAdd = valueToAdd + (currentMonth == 12 ? 1 : 0); // Handle year rollover for December
                } else {
                    // Subtracting months, need to wrap around start of month
                    int currentMonth = iChronology.getMonthOfYear(instant);
                    monthsToAdd = valueToAdd - (currentMonth == 1 ? 1 : 0); // Handle year roll back for January
                }
                
                instant = iChronology.monthOfYear().set(instant, (int)(iChronology.getMonthOfYear(instant) + monthsToAdd) % iMax);
            } else {
                // Set other fields normally, assuming the chronology handles setting correctly
                instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
            }
        }
        
        if (valueToAdd < 0) { // Subtracting months might need adjustment for end of month
            int currentMonth = iChronology.getMonthOfYear(instant);
            if (currentMonth == 1) {
                instant = iChronology.year().set(instant, iChronology.getYear(instant) - 1); // Adjust year if January is subtracted
            }
        }

        instant = add(instant, valueToAdd); // Apply the addition/subtraction across all fields
        return iChronology.get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}