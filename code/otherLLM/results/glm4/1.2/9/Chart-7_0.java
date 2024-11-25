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

    // Ensure correct calculation of the middle time for periods
    long minMiddle, maxMiddle;

    // Initialize with current period's middle
    minMiddle = middle;
    maxMiddle = middle;

    if (this.minMiddleIndex >= 0) {
        TimePeriod existingPeriod = getDataItem(this.minMiddleIndex).getPeriod();
        minMiddle = existingPeriod.getStart().getTime() + ((existingPeriod.getEnd().getTime() - existingPeriod.getStart().getTime()) / 2);
        if (middle < minMiddle) {
            this.minMiddleIndex = index;
        }
    } else {
        this.minMiddleIndex = index;
    }

    if (this.maxMiddleIndex >= 0) {
        TimePeriod existingPeriod = getDataItem(this.maxMiddleIndex).getPeriod();
        maxMiddle = existingPeriod.getStart().getTime() + ((existingPeriod.getEnd().getTime() - existingPeriod.getStart().getTime()) / 2);
        if (middle > maxMiddle) {
            this.maxMiddleIndex = index;
        }
    } else {
        this.maxMiddleIndex = index;
    }

    // Update minEnd and maxEnd indices
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