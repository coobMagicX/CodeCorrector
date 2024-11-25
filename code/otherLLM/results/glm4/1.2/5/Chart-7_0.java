private void updateBounds(TimePeriod period, int index) {

    long start = period.getStart().getTime();
    long end = period.getEnd().getTime();
    long middle = start + ((end - start) / 2);

    // Update minStartIndex and maxStartIndex
    if (this.minStartIndex >= 0 || this.maxStartIndex < 0) {
        long currentMinStart = (this.minStartIndex >= 0 ? getDataItem(this.minStartIndex).getPeriod().getStart().getTime() : Long.MAX_VALUE);
        long currentMaxStart = (this.maxStartIndex >= 0 ? getDataItem(this.maxStartIndex).getPeriod().getStart().getTime() : Long.MIN_VALUE);

        if (start < currentMinStart) {
            this.minStartIndex = index;
        }

        if (start > currentMaxStart) {
            this.maxStartIndex = index;
        }
    } else {
        this.minStartIndex = this.maxStartIndex = index;
    }

    // Update minMiddleIndex and maxMiddleIndex
    if (this.minMiddleIndex >= 0 || this.maxMiddleIndex < 0) {
        long currentMinMiddle = (this.minMiddleIndex >= 0 ? getDataItem(this.minMiddleIndex).getPeriod().getStart() + (getDataItem(this.minMiddleIndex).getPeriod().getEnd() - getDataItem(this.minMiddleIndex).getPeriod().getStart()) / 2 : Long.MAX_VALUE);
        long currentMaxMiddle = (this.maxMiddleIndex >= 0 ? getDataItem(this.maxMiddleIndex).getPeriod().getStart() + (getDataItem(this.maxMiddleIndex).getPeriod().getEnd() - getDataItem(this.maxMiddleIndex).getPeriod().getStart()) / 2 : Long.MIN_VALUE);

        if (middle < currentMinMiddle) {
            this.minMiddleIndex = index;
        }

        if (middle > currentMaxMiddle) {
            this.maxMiddleIndex = index;
        }
    } else {
        this.minMiddleIndex = this.maxMiddleIndex = index;
    }

    // Update minEndIndex and maxEndIndex
    if (this.minEndIndex >= 0 || this.maxEndIndex < 0) {
        long currentMinEnd = (this.minEndIndex >= 0 ? getDataItem(this.minEndIndex).getPeriod().getEnd().getTime() : Long.MAX_VALUE);
        long currentMaxEnd = (this.maxEndIndex >= 0 ? getDataItem(this.maxEndIndex).getPeriod().getEnd().getTime() : Long.MIN_VALUE);

        if (end < currentMinEnd) {
            this.minEndIndex = index;
        }

        if (end > currentMaxEnd) {
            this.maxEndIndex = index;
        }
    } else {
        this.minEndIndex = this.maxEndIndex = index;
    }
}