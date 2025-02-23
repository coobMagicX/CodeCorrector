public TimeSeries createCopy(RegularTimePeriod start, RegularTimePeriod end)
    throws CloneNotSupportedException {

    if (start == null) {
        throw new IllegalArgumentException("Null 'start' argument.");
    }
    if (end == null) {
        throw new IllegalArgumentException("Null 'end' argument.");
    }
    if (start.compareTo(end) > 0) {
        throw new IllegalArgumentException("Requires start on or before end.");
    }

    int startIndex = getIndex(start);
    int endIndex = getIndex(end);
    boolean emptyRange = false;

    
    if (startIndex < 0) {
        startIndex = -startIndex - 1;
        if (startIndex >= this.data.size()) {
            emptyRange = true;  
        }
    }

    
    if (endIndex < 0) {
        endIndex = -endIndex - 2;  
        if (endIndex < 0 || endIndex >= this.data.size()) {
            endIndex = this.data.size() - 1;  
        }
    }

    
    if (endIndex < startIndex) {
        emptyRange = true;
    }

    
    if (emptyRange) {
        TimeSeries copy = (TimeSeries) super.clone();
        copy.data = new java.util.ArrayList();
        return copy;
    } else {
        