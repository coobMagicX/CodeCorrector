public TimeSeries createCopy(RegularTimePeriod start, RegularTimePeriod end)
throws CloneNotSupportedException {

    if (start == null) {
        throw new IllegalArgumentException("Null 'start' argument.");
    }
    if (end == null) {
        throw new IllegalArgumentException("Null 'end' argument.");
    }
    if (start.compareTo(end) > 0) {
        throw new IllegalArgumentException(
                "Start period must not be after end period.");
    }

    int startIndex = getIndex(start);
    int endIndex = getIndex(end);

    // Check for start and end not being in the series
    if (startIndex < 0 || endIndex < 0) {
        throw new IllegalArgumentException("The specified periods are out of bounds.");
    }

    // Corrected logic to check if range is empty or not
    boolean emptyRange = startIndex >= endIndex;

    TimeSeries copy;
    if (emptyRange) {
        copy = (TimeSeries) this.clone();
        copy.data = new java.util.ArrayList<>();
    } else {
        copy = (TimeSeries) this.clone(); // Clone the entire series first

        // Remove elements before 'start'
        while (!this.data.isEmpty() && startIndex < 0) {
            startIndex = -(startIndex + 1);
            if (startIndex == this.data.size()) {
                break; // No more items to remove
            }
            copy.data.remove(0); // Remove the first item from the clone's data
        }

        // Remove elements after 'end'
        while (!this.data.isEmpty() && endIndex < this.data.size()) {
            copy.data.remove(copy.data.size() - 1); // Remove the last item from the clone's data
        }
    }
    return copy;
}