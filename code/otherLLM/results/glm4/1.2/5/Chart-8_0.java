public class Week {
    private final Date time;
    private final TimeZone zone;
    private final Locale locale;

    public Week(Date time, TimeZone zone, Locale locale) {
        this.time = time;
        this.zone = (zone != null) ? zone : RegularTimePeriod.DEFAULT_TIME_ZONE;
        this.locale = (locale != null) ? locale : Locale.getDefault();
    }

    public static Week parseWeek(String s) {
        Week result = null;
        if (s != null) {
            // trim whitespace from either end of the string
            s = s.trim();

            int i = Week.findSeparator(s);
            if (i != -1) {
                String s1 = s.substring(0, i).trim();
                String s2 = s.substring(i + 1, s.length()).trim();

                Year y = Week.evaluateAsYear(s1);
                int w;
                if (y != null) {
                    w = Week.stringToWeek(s2);
                    if (w == -1) {
                        throw new TimePeriodFormatException(
                                "Can't evaluate the week.");
                    }
                    result = new Week(time, RegularTimePeriod.DEFAULT_TIME_ZONE, Locale.getDefault()); // Use default locale and zone for consistency
                } else {
                    y = Week.evaluateAsYear(s2);
                    if (y != null) {
                        w = Week.stringToWeek(s1);
                        if (w == -1) {
                            throw new TimePeriodFormatException(
                                    "Can't evaluate the week.");
                        }
                        result = new Week(time, RegularTimePeriod.DEFAULT_TIME_ZONE, Locale.getDefault()); // Use default locale and zone for consistency
                    } else {
                        throw new TimePeriodFormatException(
                                "Can't evaluate the year.");
                    }
                }

            } else {
                throw new TimePeriodFormatException(
                        "Could not find separator.");
            }

        }
        return result;
    }

    public int getWeek() {
        Calendar calendar = Calendar.getInstance(zone, locale);
        calendar.setTime(time);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }
}