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
    if (startIndex < 0) {
        
        startIndex = -(startIndex + 1);
        
        if (startIndex >= this.data.size()) {
            startIndex = this.data.size(); 
        }
    }

    
    int endIndex = getIndex(end);
    if (endIndex < 0) {
        
        endIndex = -(endIndex + 1) - 1; 
        if (endIndex >= this.data.size()) {
            endIndex = this.data.size() - 1; 
        }
    }

    
    boolean emptyRange = false;
    if (startIndex > endIndex) {
        
        emptyRange = true;
    }

    
    if (emptyRange) {
        TimeSeries copy = (TimeSeries) super.clone();
        copy.data = new java.util.ArrayList(); 