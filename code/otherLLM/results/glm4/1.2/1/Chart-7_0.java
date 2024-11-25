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
    } else {
        this.minStartIndex = index;
    }

    if (this.maxStartIndex >= 0) {
        long maxStart = getDataItem(this.maxStartIndex).getPeriod()
            .getStart().getTime();
        if (start > maxStart) {
            this.maxStartIndex = index;           
        }
    } else {
        this.maxStartIndex = index;
    }

    if (this.minMiddleIndex >= 0) {
        long minMiddle = getDataItem(this.minMiddleIndex).getPeriod()
            .getStart().getTime() + (getDataItem(this.minMiddleIndex)
            .getPeriod().getEnd().getTime() - getDataItem(this.minMiddleIndex)
            .getPeriod().getStart().getTime()) / 2;
        if (middle < minMiddle) {
            this.minMiddleIndex = index;           
        }
    } else {
        this.minMiddleIndex = index;
    }

    // Corrected the condition for updating maxMiddleIndex
    if (this.maxMiddleIndex >= 0) {
        long s = getDataItem(this.maxMiddleIndex).getPeriod().getStart()
            .getTime();
        long e = getDataItem(this.maxMiddleIndex).getPeriod().getEnd()
            .getTime();
        long maxMiddle = s + (e - s) / 2;
        if (middle > maxMiddle) {
            this.maxMiddleIndex = index;           
        }
    } else {
        this.maxMiddleIndex = index;
    }

    // The rest of the code remains unchanged
    if (this.minEndIndex >= 0) {
        long minEnd = getDataItem(this.minEndIndex).getPeriod().getEnd()
            .getTime();
        if (end < minEnd) {
            this.minEndIndex = index;           
        }
    } else {
        this.minEndIndex = index;
    }

    if (this.maxEndIndex >= 0) {
        long maxEnd = getDataItem(this.maxEndIndex).getPeriod().getEnd()
            .getTime();
        if (end > maxEnd) {
            this.maxEndIndex = index;           
        }
    } else {
        this.maxEndIndex = index;
    }
}