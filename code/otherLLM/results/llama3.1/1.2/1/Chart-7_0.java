public TimeSeries(String domainDescription, String rangeDescription) {
    this.domainDescription = domainDescription;
    this.rangeDescription = rangeDescription;
}

// Add data validation to ensure unique time periods

@Override
public void add(TimePeriod period) {
    if (!data.contains(period)) {
        data.add(period);
        // Update min and max start, middle, end indices accordingly
    } else {
        System.out.println("Time period already exists.");
    }
}

// Handle exceptions in the clone() method

@Override
public Object clone() throws CloneNotSupportedException {
    try {
        return super.clone();
    } catch (CloneNotSupportedException e) {
        // Handle exception here (e.g., throw a custom exception)
    }
}