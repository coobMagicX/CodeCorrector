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

    // Fixed the logic for updating maxMiddleIndex
    long maxMiddle = -1;
    for (int i = 0; i < getDataItemCount(); ++i) {
        TimePeriod tempPeriod = getDataItem(i).getPeriod();
        long tempMiddle = tempPeriod.getStart().getTime() + ((tempPeriod.getEnd().getTime() - tempPeriod.getStart().getTime()) / 2);
        if (this.maxMiddleIndex >= 0 && tempMiddle > getDataItem(this.maxMiddleIndex).getPeriod().getStart().getTime() + ((getDataItem(this.maxMiddleIndex).getPeriod().getEnd().getTime() - getDataItem(this.maxMiddleIndex).getPeriod().getStart().getTime()) / 2)) {
            this.maxMiddleIndex = i;
        } else if (this.maxMiddleIndex < 0 || tempMiddle > maxMiddle) {
            this.maxMiddleIndex = i;
            maxMiddle = tempMiddle;
        }
    }

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