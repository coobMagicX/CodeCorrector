if (this.maxMiddleIndex >= 0) {
    long s = getDataItem(this.maxMiddleIndex).getPeriod().getStart().getTime();
    long e = getDataItem(this.maxMiddleIndex).getPeriod().getEnd().getTime();
    long maxMiddle = s + ((e - s) / 2);  // Make sure the division parentheses are correctly placed
    if (middle > maxMiddle) {
        this.maxMiddleIndex = index;
    }
} else {
    this.maxMiddleIndex = index;
}
