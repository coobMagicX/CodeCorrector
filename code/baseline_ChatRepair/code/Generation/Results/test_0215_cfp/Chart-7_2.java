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

    
    if (this.minMiddleIndex < 0 || middle < getMiddleTime(this.minMiddleIndex)) {
        this.minMiddleIndex = index;
    }

    
    if (this.maxMiddleIndex < 0 || middle > getMiddleTime(this.maxMiddleIndex)) {
        this.maxMiddleIndex = index;
    }

    
    if (this.minEndIndex < 0 || end < getDataItem(this.minEndIndex).getPeriod().getEnd().getTime()) {
        this.minEndIndex = index;
    }
    
    
    if (this.maxEndIndex < 0 || end > getDataItem(this.maxEndIndex).getPeriod().getEnd().getTime()) {
        this.maxEndIndex = index;
    }
}


private long getMiddleTime(int index) {
    long s = getDataItem(index).getPeriod().getStart().getTime();
    long e = getDataItem(index).getPeriod().getEnd().getTime();
    return s + ((e - s) / 2);
}
