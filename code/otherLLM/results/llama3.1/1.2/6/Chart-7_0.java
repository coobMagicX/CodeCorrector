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
    
    // Corrected logic to update maxMiddleIndex
    long s = getDataItem(this.minMiddleIndex).getPeriod().getStart()
            .getTime();
    long e = getDataItem(this.minMiddleIndex).getPeriod().getEnd()
            .getTime();
    long maxMiddle = s + (e - s) / 2;
    
    if (this.maxMiddleIndex >= 0) {
        // Compare the calculated middle value with the maximum middle value from other data items
        if ((middle > maxMiddle && index < this.maxMiddleIndex) || 
            (this.maxMiddleIndex == index && !period.getPeriod().equals(getDataItem(this.maxMiddleIndex).getPeriod()))) {
            this.maxMiddleIndex = index;           
        }
    }
    else {
        // If maxMiddleIndex is not set, update it with the current index
        this.maxMiddleIndex = index;
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