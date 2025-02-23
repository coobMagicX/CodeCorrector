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
        
        startIndex = - (startIndex + 1);
        
        
        if (startIndex >= this.data.size()) {
            emptyRange = true;  
        }
    }
    
    if (endIndex < 0) {
        
        endIndex = -(endIndex + 1) - 1;

        
        if (endIndex < 0) {
            emptyRange = true;
        }
    }

    
    endIndex = Math.min(endIndex, this.data.size() - 1);

    
    if (emptyRange || endIndex < startIndex) {
        TimeSeries copy = (TimeSeries) super.clone();
        copy.data = new java.util.ArrayList();
        return copy;
    }
    else {
        return createCopy(startIndex, endIndex);
    }
}
