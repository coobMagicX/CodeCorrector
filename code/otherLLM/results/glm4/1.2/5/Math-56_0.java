public int[] getCounts(int index) {
    if (index < 0 ||
        index >= totalSize) {
        throw new OutOfRangeException(index, 0, totalSize);
    }

    final int[] indices = new int[dimension];

    // Initialize the count to represent the cumulative index from previous dimensions.
    int count = 0;
    for (int i = 0; i < last; i++) {
        int idx = 0;
        final int offset = uniCounterOffset[i];
        while (count <= index) { // Ensure we do not exceed the index
            count += offset;
            ++idx;
        }
        indices[i] = idx; // Assign the calculated index before adjusting count to prevent a double count.
        count -= offset; // Adjust count back after assigning the index.
    }

    // Calculate the remaining index for the last dimension.
    int remainder = index - (count - uniCounterOffset[last]);
    indices[last] = remainder;

    return indices;
}