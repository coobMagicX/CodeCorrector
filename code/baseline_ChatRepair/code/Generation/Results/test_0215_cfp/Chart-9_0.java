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
    
    
    if (startIndex < 0) {
        startIndex = -(startIndex + 1); 
        if (startIndex >= this.data.size()) {
            emptyRange = true;  
        }
    }

    int endIndex = getIndex(end);
    
    
    if (endIndex < 0) {
        endIndex = -(endIndex + 1) - 1; 
        if (endIndex >= this.data.size()) {
            endIndex = this.data.size() - 1; 
        }
    } else if (endIndex >= this.data.size()) {
        endIndex = this.data.size() - 1; 
    }

    
    if (endIndex < 0 || endIndex < startIndex) {
        emptyRange = true;
    }

    
    if (emptyRange) {
        TimeSeries copy =