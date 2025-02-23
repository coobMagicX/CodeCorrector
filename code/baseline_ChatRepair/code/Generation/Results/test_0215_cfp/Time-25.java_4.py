public int getOffsetFromLocal(long instantLocal) {
    // Get the initial estimate of the offset using the local timestamp
    int offsetEstimate = getOffset(instantLocal);

    // Calculate the instant in UTC using the initial estimated offset
    long instantUTC = instantLocal - offsetEstimate;

    // Get the actual offset from the UTC instant (check if DST applies here)
    int actualOffset = getOffset(instantUTC);

    // If the two offsets are the same, we can simply return one of them
    if (offsetEstimate == actualOffset) {
        return actualOffset;
    }

    // Calculate the local instant again using the actual offset
    long adjustedInstantLocal = instantUTC + actualOffset;

    // If the adjusted local instant gives us the same local timestamp as initially provided
    if (adjustedInstantLocal == instantLocal) {
        return actualOffset;
    }

    // If we reach this line, it means the transition did not preserve local time.
    // We must resolve a corner case typically around midnight transitions.
    // Does re-calculation shift the instant across a DST boundary?
    if (getOffset(instantUTC + actualOffset) == getOffset(instantUTC - actualOffset)) {
        return actualOffset;
    }

    // In a rare case (like negative DST change), return the first estimate
    // when it actually crosses midnight and the above checks are invalidated
    return offsetEstimate;
}
