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

    boolean emptyRange = false;
    int startIndex = getIndex(start);
    int endIndex = getIndex(end);

    if (startIndex < 0) {
        
        startIndex = -(startIndex + 1);
        if (startIndex >= this.data.size()) {
            emptyRange = true;  
            startIndex = this.data.size(); 
        }
    }

    if (endIndex < 0) {
        
        endIndex = -(endIndex + 1) - 1; 
        if (endIndex >= this.data.size()) {
            endIndex = this.data.size() - 1; 
        }
    }

    if (endIndex < startIndex || startIndex >= this.data.size() || endIndex < 0) {
        emptyRange = true; 
    }

    if (emptyRange) {
        TimeSeries copy = (TimeSeries) super.clone();
        copy.data = new java.util.ArrayList();
    } else {
        return createCopy(startIndex, endIndex);
    }
}
