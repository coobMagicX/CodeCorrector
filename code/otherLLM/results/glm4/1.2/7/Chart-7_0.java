private void updateBounds(TimePeriod period, int index) {

    long start = period.getStart().getTime();
    long end = period.getEnd().getTime();
    long middle = start + ((end - start) / 2);

    if (this.minStartIndex < 0) {
        this.minStartIndex = index;
    }
    if (start < getDataItem(this.minStartIndex).getPeriod().getStart().getTime()) {
        this.minStartIndex = index;
    }

    if (this.maxStartIndex < 0) {
        this.maxStartIndex = index;
    }
    if (start > getDataItem(this.maxStartIndex).getPeriod().getStart().getTime()) {
        this.maxStartIndex = index;
    }

    if (this.minMiddleIndex < 0) {
        this.minMiddleIndex = index;
    } else {
        long minMiddle = getDataItem(this.minMiddleIndex).getPeriod().getStart() + 
                          (getDataItem(this.minMiddleIndex).getPeriod().getEnd() - 
                           getDataItem(this.minMiddleIndex).getPeriod().getStart()) / 2;
        if (middle < minMiddle) {
            this.minMiddleIndex = index;
        }
    }

    if (this.maxMiddleIndex < 0) {
        this.maxMiddleIndex = index;
    } else {
        long maxMiddle = getDataItem(this.maxMiddleIndex).getPeriod().getStart() + 
                          (getDataItem(this.maxMiddleIndex).getPeriod().getEnd() - 
                           getDataItem(this.maxMiddleIndex).getPeriod().getStart()) / 2;
        if (middle > maxMiddle) {
            this.maxMiddleIndex = index;
        }
    }

    if (this.minEndIndex < 0) {
        this.minEndIndex = index;
    }
    if (end < getDataItem(this.minEndIndex).getPeriod().getEnd().getTime()) {
        this.minEndIndex = index;
    }

    if (this.maxEndIndex < 0) {
        this.maxEndIndex = index;
    }
    if (end > getDataItem(this.maxEndIndex).getPeriod().getEnd().getTime()) {
        this.maxEndIndex = index;
    }
}