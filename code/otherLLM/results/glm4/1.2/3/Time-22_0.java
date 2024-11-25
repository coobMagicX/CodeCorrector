import org.threeten.bp.ChronoUnit;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ReadableInstant;

protected BasePeriod(long duration) {
    long weeks = duration / (24 * 60 * 60 * 1000L); // Convert milliseconds to days, then to weeks
    long remainingDays = (duration % (24 * 60 * 60 * 1000L)) / (24 * 60 * 60 * 1000L); // Calculate remaining days

    LocalDate startLocalDate = LocalDate.now(); // Assuming current date for simplicity, adjust as necessary
    LocalDate endLocalDate = startLocalDate.plusWeeks(weeks).plusDays(remainingDays);

    this.startInstant = DateTimeUtils.fromMillis(startLocalDate.atStartOfDay().toInstant());
    this.endInstant = DateTimeUtils.fromMillis(endLocalDate.atTime(0, 0, 0, 0).toInstant());
}

// Helper methods to convert between LocalDate and ReadableInstant
private static ReadableInstant fromMillis(long millis) {
    return Instant.ofEpochMilli(millis);
}