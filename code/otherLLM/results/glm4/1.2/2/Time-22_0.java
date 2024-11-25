import java.time.Duration;
import java.time.Period;

protected BasePeriod(long duration) {
    this(duration, Duration.ofSeconds(0), Duration.ofSeconds(0));
}

protected BasePeriod(long totalDays) {
    // Assuming the method is meant to convert a long representing total days into a Period object

    int weeks = (int)(totalDays / 7);
    int days = (int)(totalDays % 7);

    this.period = Period.of(weeks, 0, days); // This creates a Period with the correct number of weeks and days
}