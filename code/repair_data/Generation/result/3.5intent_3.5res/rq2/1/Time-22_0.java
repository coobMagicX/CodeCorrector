protected BasePeriod(long duration) {
    this(duration, null, null);
    // bug [3264409]
}

public int size() {
    return iType.size();
}