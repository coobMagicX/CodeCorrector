protected BasePeriod(long duration) {
    long days = duration;
    weeks = (days / 7);
    this(duration, null, null);
}