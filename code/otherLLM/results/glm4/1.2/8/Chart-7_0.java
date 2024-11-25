private void updateBounds(TimePeriod period, int index) {

    long start = period.getStart().getTime();
    long end = period.getEnd().getTime();
    long middle = start + ((end - start) / 2);

    if (this.minStartIndex >= 0 && start < getDataItem(this.minStartIndex).getPeriod()
            .getStart().getTime()) {
        this.minStartIndex = index;
    } else if (this.minStartIndex < 0) {
        this.minStartIndex = index;
    }

    if (this.maxStartIndex >= 0 && start > getDataItem(this.maxStartIndex).getPeriod()
            .getStart().getTime()) {
        this.maxStartIndex = index;
    } else if (this.maxStartIndex < 0) {
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
        long s = period.getStart().getTime();
        long e = period.getEnd().getTime();
        long minMiddle = s + (e - s) / 2;
        if (middle < minMiddle) {
            this.minMiddleIndex = index;
        }
    }

    if (this.maxMiddleIndex >= 0) {
        long maxMiddle = getDataItem(this.maxMiddleIndex).getPeriod()
            .getStart().getTime() + (getDataItem(this.maxMiddleIndex)
                .getPeriod().getEnd().getTime() - getDataItem(this.maxMiddleIndex)
                .getPeriod().getStart().getTime()) / 2;
        if (middle > maxMiddle) {
            this.maxMiddleIndex = index;
        }
    } else {
        long s = period.getStart().getTime();
        long e = period.getEnd().getTime();
        long maxMiddle = s + (e - s) / 2;
        if (middle > maxMiddle) {
            this.maxMiddleIndex = index;
        }
    }

    if (this.minEndIndex >= 0 && end < getDataItem(this.minEndIndex).getPeriod()
            .getEnd().getTime()) {
        this.minEndIndex = index;
    } else if (this.minEndIndex < 0) {
        this.minEndIndex = index;
    }

    if (this.maxEndIndex >= 0 && end > getDataItem(this.maxEndIndex).getPeriod()
            .getEnd().getTime()) {
        this.maxEndIndex = index;
    } else if (this.maxEndIndex < 0) {
        this.maxEndIndex = index;
    }
}