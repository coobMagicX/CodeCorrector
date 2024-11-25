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
    }
    else {
        this.minStartIndex = index;
    }
    
    if (this.maxStartIndex >= 0) {
        long maxStart = getDataItem(this.maxStartIndex).getPeriod()
            .getStart().getTime();
        if (start > maxStart) {
            this.maxStartIndex = index;           
        }
    }
    else {
        this.maxStartIndex = index;
    }
    
    long minMiddle = getMinMiddleValue(index);
    long maxMiddle = getMaxMiddleValue(index);

    if (this.minMiddleIndex >= 0) {
        if (middle < getDataItem(this.minMiddleIndex).getPeriod().getStart().getTime() + ((getDataItem(this.minMiddleIndex).getPeriod().getEnd().getTime() - getDataItem(this.minMiddleIndex).getPeriod().getStart().getTime()) / 2)) {
            this.minMiddleIndex = index;           
        }
    }
    else {
        this.minMiddleIndex = index;
    }

    if (this.maxMiddleIndex >= 0) {
        if (middle > getMaxMiddleValue(index)) {
            this.maxMiddleIndex = index;           
        }
    }
    else {
        this.maxMiddleIndex = index;
    }
    
    if (this.minEndIndex >= 0) {
        long minEnd = getDataItem(this.minEndIndex).getPeriod().getEnd()
            .getTime();
        if (end < minEnd) {
            this.minEndIndex = index;           
        }
    }
    else {
        this.minEndIndex = index;
    }
   
    if (this.maxEndIndex >= 0) {
        long maxEnd = getDataItem(this.maxEndIndex).getPeriod().getEnd()
            .getTime();
        if (end > maxEnd) {
            this.maxEndIndex = index;           
        }
    }
    else {
        this.maxEndIndex = index;
    }
    
}

private long getMinMiddleValue(int index) {
    TimePeriodValue tpv = getDataItem(index);
    return tpv.getPeriod().getStart().getTime() + ((tpv.getPeriod().getEnd().getTime() - tpv.getPeriod().getStart().getTime()) / 2);
}

private long getMaxMiddleValue(int index) {
    TimePeriodValue tpv = getDataItem(index);
    return tpv.getPeriod().getStart().getTime() + (((tpv.getPeriod().getEnd().getTime() - tpv.getPeriod().getStart().getTime()) / 2)) * 2;
}