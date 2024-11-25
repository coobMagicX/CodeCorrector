public int[] getCounts(int index) {
    if (index < 0 ||
        index >= totalSize) {
        throw new OutOfRangeException(index, 0, totalSize);
    }

    final int[] indices = new int[dimension];

    // The outer loop now terminates correctly after processing all dimensions except the last one.
    for (int i = 0; i < dimension - 1; i++) {
        int idx = 0;
        final int offset = uniCounterOffset[i];
        while (count + offset <= index) { // Ensure we do not go past the index
            count += offset;
            ++idx;
        }
        --idx; // Adjust for the last iteration that exceeds the index
        indices[i] = idx;
    }

    // Now we calculate for the last dimension separately
    int lastIdx = 0;
    while (count < index) {
        count += lastIdx + 1; // Start from the next integer after the current value of count
        ++lastIdx;
    }
    --lastIdx; // Adjust for the last iteration that exceeds the index
    indices[dimension - 1] = lastIdx;

    return indices;
}