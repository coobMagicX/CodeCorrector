public int[] getCounts(int index) {
    if (index < 0 || 
        index >= totalSize) { // Add bounds checking for the last dimension
        throw new OutOfRangeException(index, 0, totalSize);
    }

    final int[] indices = new int[dimension];

    int count = 0;
    for (int i = 0; i < last; i++) {
        int idx = 0;
        final int offset = uniCounterOffset[i];
        while (count <= index) { // Check if count exceeds index
            count += offset;
            ++idx;
        }
        --idx;
        count -= offset;
        indices[i] = idx;
    }

    int lastIdx = 1; // Initialize lastIdx before the while loop
    while (count < index) {
        count += lastIdx;
        ++lastIdx;
    }
    --lastIdx;
    if (lastIdx > size[last]) { // Check bounds for the last dimension
        throw new OutOfRangeException(lastIdx, 1, size[last]);
    }
    indices[last] = lastIdx;

    return indices;
}