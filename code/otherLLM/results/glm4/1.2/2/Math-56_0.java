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
        while (count + offset <= index) { // Fixed condition to ensure we do not go beyond the index
            count += offset;
            ++idx;
        }
        --idx; // Decrement idx after the loop, as it goes one step past the last valid value
        count -= offset; // Adjust count back by the offset since we went too far in the while condition
        indices[i] = idx;
    }

    int remainingIndex = index - count; // Calculate remaining index to be assigned
    for (int i = last; i < dimension; i++) { // Iterate over any additional dimensions not covered by uniCounterOffset
        indices[i] = remainingIndex % uniCounterOffset[i]; // Use modulo to find the appropriate index
        remainingIndex /= uniCounterOffset[i]; // Update remainingIndex for the next iteration
    }

    return indices;
}