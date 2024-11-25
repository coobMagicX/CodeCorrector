protected BasePeriod(long duration) {
    this(duration, null, null);
    long totalWeeks = (duration + 3 * 86400 * 1000L - 1) / (7 * 24 * 60 * 60 * 1000); // Adjust for rounding issues
    this.weeks = totalWeeks;
}