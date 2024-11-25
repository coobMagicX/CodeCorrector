import org.joda.time.DateTimeUtils;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;

public class DateCalculator {

    private ISOChronology iChronology = ISOChronology.getInstanceUTC();

    public boolean isLeap(long instant) {
        int thisYear = iChronology.getYear(instant);
        if (iChronology.isLeapYear(thisYear)) {
            return (iChronology.getMonthOfYear(instant, thisYear) == 2); // February
        }
        return false;
    }

    private long add(long instant, int valueToAdd) {
        long result = instant;
        while (valueToAdd != 0) {
            boolean isAddingMonth = iChronology.getFieldType(ISOChronology.FIELD_MONTH).getField(iChronology).isSupported(instant);
            boolean isLeapYear = isLeap(result);

            if (isAddingMonth && valueToAdd < 0) {
                // When subtracting months
                int month = iChronology.getMonthOfYear(result);
                int year = iChronology.getYear(result);
                while (month <= 1) { // Move to previous year
                    month += 12;
                    year -= 1;
                }
                month += valueToAdd; // Adjust the month by the subtraction amount
                if (isLeapYear && month > 2 && month < 13) {
                    // If the result is in a leap year, adjust for February 29th
                    if (month == 3) {
                        month = 1;
                        year += 1; // Next year for non-leap year
                    }
                } else {
                    month = iChronology.getMonthOfYear(result);
                    while (month < 1) { // Move to previous month
                        month += 12;
                        year -= 1;
                    }
                }

                result = iChronology.setMonthYearDayHourMinuteSecondMillisecond(
                        result,
                        month,
                        isLeapYear ? 29 : 28, // February for leap years, otherwise 28
                        iChronology.getDayOfMonth(result),
                        iChronology.getHourOfDay(result),
                        iChronology.getMinuteOfHour(result),
                        iChronology.getSecondOfMinute(result),
                        iChronology.getMillisecondOfSecond(result)
                );
            } else {
                // Normal addition
                result = DateTimeUtils.addMonths(instant, valueToAdd);
            }
            valueToAdd += (valueToAdd < 0) ? 1 : -1; // Adjust the sign of valueToAdd for the next iteration
        }
        return result;
    }

    public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
        if (valueToAdd == 0) {
            return values;
        }
        if (DateTimeUtils.isContiguous(partial)) {
            long instant = 0L;
            for (int i = 0, isize = partial.size(); i < isize; i++) {
                instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
            }
            instant = add(instant, valueToAdd);
            return iChronology.get(partial, instant);
        } else {
            return super.add(partial, fieldIndex, values, valueToAdd); // Assuming the superclass has a correct implementation
        }
    }
}