protected BasePeriod(long duration) {
    long days = duration / getDayDuration();
    long remainingDays = duration % getDayDuration();

    long weeks = remainingDays / getWeekDuration();
    long remainingSeconds = (remainingDays % getWeekDuration()) * 86400; // convert seconds to days

    long hours = remainingSeconds / 3600;
    long minutes = (remainingSeconds % 3600) / 60;
    long seconds = remainingSeconds % 60;

    this(duration, weeks, new BasePeriod(days));
}

public int getWeeks() {
    return weeks;
}