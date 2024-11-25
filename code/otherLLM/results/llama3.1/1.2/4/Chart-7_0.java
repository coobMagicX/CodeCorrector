private void updateBounds(TimePeriod period, int index) {
    
    long start = period.getStart().getTime();
    long end = period.getEnd().getTime();
    long middle = start + ((end - start) / 2);

    if (this.minStartIndex >= 0) {
        long minStart = getDataItem(this.minStartIndex).getPeriod()
            .getStart().getTime();
        if (start < minStart) {
            this.minStartIndex = index;           
        }
    }
    else {
        this.minStartIndex = index;
    }
    
    if (this.maxStartIndex >= 0) {
        long maxStart = getDataItem(this.maxStartIndex).getPeriod()
            .getStart().getTime();
        if (start > maxStart) {
            this.maxStartIndex = index;           
        }
    }
    else {
        this.maxStartIndex = index;
    }
    
    // Modified the minMiddleIndex update logic to use the period's end time
    long s = getDataItem(this.minMiddleIndex).getPeriod().getEnd()
            .getTime();
    long e = getDataItem(this.minMiddleIndex).getPeriod().getEnd()
            .getTime();
    long minMiddle = s + (e - s) / 2;
    
    // Added a check to update maxMiddleIndex when the current middle is greater than the minMiddle
    if ((this.maxMiddleIndex >= 0 && middle > getDataItem(this.maxMiddleIndex).getPeriod().getEnd().getTime()) 
        || this.maxMiddleIndex < 0) {
        this.maxMiddleIndex = index;
    }
    
    // Added a check to update maxMiddleIndex when the current middle is greater than the minMiddle
    if (this.minMiddleIndex >= 0 && middle < getDataItem(this.minMiddleIndex).getPeriod().getEnd().getTime()) {
        this.minMiddleIndex = index;           
    }
    else {
        this.minMiddleIndex = index;
    }
    
    if (this.minEndIndex >= 0) {
        long minEnd = getDataItem(this.minEndIndex).getPeriod().getEnd()
            .getTime();
        if (end < minEnd) {
            this.minEndIndex = index;           
        }
    }
    else {
        this.minEndIndex = index;
    }
   
    if (this.maxEndIndex >= 0) {
        long maxEnd = getDataItem(this.maxEndIndex).getPeriod().getEnd()
            .getTime();
        if (end > maxEnd) {
            this.maxEndIndex = index;           
        }
    }
    else {
        this.maxEndIndex = index;
    }
    
}