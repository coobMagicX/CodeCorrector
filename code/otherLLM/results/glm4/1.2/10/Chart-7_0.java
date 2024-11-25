private void updateBounds(TimePeriod period, int index) {

    long start = period.getStart().getTime();
    long end = period.getEnd().getTime();
    long middle = start + ((end - start) / 2);

    // Initialize min and max indices for start, middle, and end times
    if (this.minStartIndex < 0 || start < getDataItem(this.minStartIndex).getPeriod().getStart().getTime()) {
        this.minStartIndex = index;
    }
    
    if (this.maxStartIndex < 0 || start > getDataItem(this.maxStartIndex).getPeriod().getStart().getTime()) {
        this.maxStartIndex = index;
    }

    // Correct the logic for minMiddleIndex and maxMiddleIndex by removing the incorrect call to minMiddleIndex
    if (this.minMiddleIndex < 0 || middle < getDataItem(this.minMiddleIndex).getPeriod().getStart() + (getDataItem(this.minMiddleIndex).getPeriod().getEnd() - getDataItem(this.minMiddleIndex).getPeriod().getStart()) / 2) {
        this.minMiddleIndex = index;
    }
    
    if (this.maxMiddleIndex < 0 || middle > getDataItem(this.maxMiddleIndex).getPeriod().getStart() + (getDataItem(this.maxMiddleIndex).getPeriod().getEnd() - getDataItem(this.maxMiddleIndex).getPeriod().getStart()) / 2) {
        this.maxMiddleIndex = index;
    }

    // Initialize min and max indices for end times
    if (this.minEndIndex < 0 || end < getDataItem(this.minEndIndex).getPeriod().getEnd().getTime()) {
        this.minEndIndex = index;
    }
    
    if (this.maxEndIndex < 0 || end > getDataItem(this.maxEndIndex).getPeriod().getEnd().getTime()) {
        this.maxEndIndex = index;
    }
}