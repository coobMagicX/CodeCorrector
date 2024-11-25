private void updateBounds(TimePeriod period, int index) {

    long start = period.getStart().getTime();
    long end = period.getEnd().getTime();
    long middle = start + ((end - start) / 2);

    // Update minStartIndex if this is the earliest start time
    if (this.minStartIndex < 0 || start < getDataItem(this.minStartIndex).getPeriod().getStart().getTime()) {
        this.minStartIndex = index;
    }

    // Update maxStartIndex if this is the latest start time
    if (this.maxStartIndex < 0 || start > getDataItem(this.maxStartIndex).getPeriod().getStart().getTime()) {
        this.maxStartIndex = index;
    }

    // Update minMiddleIndex if this has a smaller middle time than the current minimum
    if (this.minMiddleIndex < 0 || (this.minMiddleIndex >= 0 && middle < getDataItem(this.minMiddleIndex).getPeriod().getStart() + (getDataItem(this.minMiddleIndex).getPeriod().getEnd() - getDataItem(this.minMiddleIndex).getPeriod().getStart()) / 2)) {
        this.minMiddleIndex = index;
    }

    // Update maxMiddleIndex if this has a larger middle time than the current maximum
    if (this.maxMiddleIndex < 0 || (this.maxMiddleIndex >= 0 && middle > getDataItem(this.maxMiddleIndex).getPeriod().getStart() + (getDataItem(this.maxMiddleIndex).getPeriod().getEnd() - getDataItem(this.maxMiddleIndex).getPeriod().getStart()) / 2)) {
        this.maxMiddleIndex = index;
    }

    // Update minEndIndex if this is the earliest end time
    if (this.minEndIndex < 0 || end < getDataItem(this.minEndIndex).getPeriod().getEnd().getTime()) {
        this.minEndIndex = index;
    }

    // Update maxEndIndex if this is the latest end time
    if (this.maxEndIndex < 0 || end > getDataItem(this.maxEndIndex).getPeriod().getEnd().getTime()) {
        this.maxEndIndex = index;
    }
}