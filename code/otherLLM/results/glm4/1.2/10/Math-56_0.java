public int[] getCounts(int index) {
    if (index < 0 ||
        index >= totalSize) {
        throw new OutOfRangeException(index, 0, totalSize);
    }

    final int[] indices = new int[dimension];

    int count = 0;
    for (int i = 0; i < last; i++) {
        int idx = 0;
        final int offset = uniCounterOffset[i];
        while (count + offset <= index) {
            count += offset;
            ++idx;
        }
        // Reset count to the value before adding offset
        count -= offset;
        indices[i] = idx;
    }

    // Calculate the last dimension index separately since it's not dependent on previous counts
    int remainingIndex = index - count; // The remaining part of the index that hasn't been accounted for yet
    if (remainingIndex > 0) {
        // Find the offset for the last dimension and calculate the last index accordingly
        int lastOffset = uniCounterOffset[last];
        while (lastOffset <= remainingIndex) {
            remainingIndex -= lastOffset;
            ++idx; // This should be initialized to 1 from the start if this is the only loop in the method
        }
    } else {
        idx = 0; // If no more increments are needed, set idx to 0
    }
    indices[last] = idx;

    return indices;
}