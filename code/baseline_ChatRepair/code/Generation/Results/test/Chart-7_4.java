private void updateBounds(TimePeriod period, int index) {
    
    long start = period.getStart().getTime();
    long end = period.getEnd().getTime();
    long middle = start + ((end - start) / 2);

    
    if (this.minStartIndex < 0 || start < getDataItem(this.minStartIndex).getPeriod().getStart().getTime()) {
        this.minStartIndex = index;
    }
    
    
    if (this.maxStartIndex < 0 || start > getDataItem(this.maxStartIndex).getPeriod().getStart().getTime()) {
        this.maxStartIndex = index;
    }
    
    
    if (this.minMiddleIndex < 0) {
        this.minMiddleIndex = index;
    } else {
        long minMiddleStart = getDataItem(this.minMiddleIndex).getPeriod().getStart().getTime();
        long minMiddleEnd = getDataItem(this.minMiddleIndex).getPeriod().getEnd().getTime();
        long minMiddle = minMiddleStart + (minMiddleEnd - minMiddleStart) / 2;
        if (middle < minMiddle) {
            this.minMiddleIndex = index;           
        }
    }
    
    
    if (this.maxMiddleIndex < 0) {
        this.maxMiddleIndex = index;
    } else {
        long maxMiddleStart = getDataItem(this.maxMiddleIndex).getPeriod().getStart().getTime();
        long maxMiddleEnd = getDataItem(this.maxMiddleIndex).getPeriod().getEnd().getTime();
        long maxMiddle = maxMiddleStart + (maxMiddleEnd - maxMiddleStart) / 2;
        if (middle > maxMiddle) {
            this.maxMiddleIndex = index;
        }
    }
    
    
    if (this.minEndIndex < 0 || end < getDataItem(this.minEndIndex).getPeriod().getEnd().getTime()) {
        this.minEndIndex = index;
    }
   
    
    if (this.maxEndIndex < 0 || end > getDataItem(this.maxEndIndex).getPeriod().getEnd().getTime()) {
        this.maxEndIndex = index;
    }
}
