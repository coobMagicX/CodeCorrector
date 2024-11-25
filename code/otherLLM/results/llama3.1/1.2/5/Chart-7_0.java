private void updateBounds(TimePeriod period, int index) {
    
    long start = period.getStart().getTime();
    long end = period.getEnd().getTime();
    long middle = start + ((end - start) / 2);

    if (this.minStartIndex >= 0) {
        TimePeriod minStartPeriod = getTimePeriod(this.minStartIndex);
        long minStart = minStartPeriod.getStart().getTime();
        if (start < minStart) {
            this.minStartIndex = index;           
        }
    }
    else {
        this.minStartIndex = index;
    }
    
    if (this.maxStartIndex >= 0) {
        TimePeriod maxStartPeriod = getTimePeriod(this.maxStartIndex);
        long maxStart = maxStartPeriod.getStart().getTime();
        if (start > maxStart) {
            this.maxStartIndex = index;           
        }
    }
    else {
        this.maxStartIndex = index;
    }
    
    if (this.minMiddleIndex >= 0) {
        TimePeriod minMiddlePeriod = getTimePeriod(this.minMiddleIndex);
        long s = minMiddlePeriod.getStart().getTime();
        long e = minMiddlePeriod.getEnd().getTime();
        long minMiddle = s + (e - s) / 2;
        if (middle < minMiddle) {
            this.minMiddleIndex = index;           
        }
    }
    else {
        this.minMiddleIndex = index;
    }
    
    if (this.maxMiddleIndex >= 0) {
        TimePeriod maxMiddlePeriod = getTimePeriod(this.maxMiddleIndex);
        long s = maxMiddlePeriod.getStart().getTime();
        long e = maxMiddlePeriod.getEnd().getTime();
        long maxMiddle = s + (e - s) / 2;
        if (middle > maxMiddle) {
            this.maxMiddleIndex = index;           
        }
    }
    else {
        this.maxMiddleIndex = index;
    }
    
    if (this.minEndIndex >= 0) {
        TimePeriod minEndPeriod = getTimePeriod(this.minEndIndex);
        long minEnd = minEndPeriod.getEnd().getTime();
        if (end < minEnd) {
            this.minEndIndex = index;           
        }
    }
    else {
        this.minEndIndex = index;
    }
   
    if (this.maxEndIndex >= 0) {
        TimePeriod maxEndPeriod = getTimePeriod(this.maxEndIndex);
        long maxEnd = maxEndPeriod.getEnd().getTime();
        if (end > maxEnd) {
            this.maxEndIndex = index;           
        }
    }
    else {
        this.maxEndIndex = index;
    }
    
}