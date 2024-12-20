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
    
    if (this.minMiddleIndex >= 0) {
        long s = getDataItem(this.minMiddleIndex).getPeriod().getStart()
            .getTime();
        long e = getDataItem(this.minMiddleIndex).getPeriod().getEnd()
            .getTime();
        long minMiddle = s + (e - s) / 2;
        if (middle < minMiddle) {
            this.minMiddleIndex = index;           
        }
    }
    else {
        this.minMiddleIndex = index;
    }
    
    // Fixed bug: Use getDataItem(index).getPeriod() instead of getDataItem(this.minMiddleIndex)
    long s = getDataItem(index).getPeriod().getStart()
            .getTime();
    long e = getDataItem(index).getPeriod().getEnd()
            .getTime();
    long maxMiddle = s + (e - s) / 2;
    
    // Fixed bug: Compare middle with maxMiddle instead of using getMaxMiddleIndex()
    if (middle > maxMiddle) {
        this.maxMiddleIndex = index;           
    }
    else {
        // If the newly added period's end time is greater than the current maxEnd, update maxEnd
        long maxEnd = getDataItem(this.maxEndIndex).getPeriod().getEnd()
            .getTime();
        if (end > maxEnd) {
            this.maxEndIndex = index;           
        }
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
}