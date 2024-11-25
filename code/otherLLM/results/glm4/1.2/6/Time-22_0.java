public class BasePeriod {
    // Assuming these fields exist and are private.
    private int years;
    private int months;
    private int days;

    public BasePeriod(long duration) {
        this.toPeriod(duration);
    }

    private void toPeriod(long duration) {
        int weeks = 0;
        long days = duration % (7 * 24 * 3600000); // milliseconds per week

        if (days > 0) {
            weeks += days / (7 * 24 * 3600000);
            days %= (7 * 24 * 3600000);
        }

        int months = 0;
        long remainingDays = duration - (weeks * 7 * 24 * 3600000); // remove the full weeks from duration

        if (remainingDays > 0) {
            months += remainingDays / (30 * 24 * 3600000); // assuming a month is approximately 30 days
            remainingDays %= (30 * 24 * 3600000);
        }

        int years = 0;
        long remainingDaysAfterMonths = duration - (months * 30 * 24 * 3600000);

        if (remainingDaysAfterMonths > 0) {
            years += remainingDaysAfterMonths / (365 * 24 * 3600000); // assuming a year is approximately 365 days
        }

        this.years = years;
        this.months = months;
        this.days = weeks; // weeks are stored as days, since we're not counting whole weeks
    }
    
    // Other methods and logic...
}