private void updateBounds(TimePeriod period, int index) {

    long start = period.getStart().getTime();
    long end = period.getEnd().getTime();
    long middle = start + ((end - start) / 2);

    if (this.minStartIndex < 0 || (start < getDataItem(this.minStartIndex).getPeriod().getStart().getTime())) {
        this.minStartIndex = index;           
    }
    
    if (this.maxStartIndex < 0 || (start > getDataItem(this.maxStartIndex).getPeriod().getStart().getTime())) {
        this.maxStartIndex = index;
    }

    if (this.minMiddleIndex < 0 || (middle < getDataItem(this.minMiddleIndex).getPeriod().getStart() + (getDataItem(this.minMiddleIndex).getPeriod().getEnd() - getDataItem(this.minMiddleIndex).getPeriod().getStart()) / 2)) {
        this.minMiddleIndex = index;
    }
    
    if (this.maxMiddleIndex < 0 || (middle > getDataItem(this.maxMiddleIndex).getPeriod().getStart() + (getDataItem(this.maxMiddleIndex).getPeriod().getEnd() - getDataItem(this.maxMiddleIndex).getPeriod().getStart()) / 2)) {
        this.maxMiddleIndex = index;
    }
    
    if (this.minEndIndex < 0 || (end < getDataItem(this.minEndIndex).getPeriod().getEnd().getTime())) {
        this.minEndIndex = index;           
    }

    if (this.maxEndIndex < 0 || (end > getDataItem(this.maxEndIndex).getPeriod().getEnd().getTime())) {
        this.maxEndIndex = index;
    }
}