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
        while (count <= index) {
            count += offset;
            ++idx;
        }
        // Adjust the logic to ensure that we are decrementing count by offset
        // after setting indices[i] to avoid moving forward in the sequence.
        if (count > index) {
            count -= offset; // This is now inside the condition.
        }
        indices[i] = idx;
    }

    int idx = 1;
    while (count < index) {
        count += idx;
        ++idx;
    }
    --idx;
    indices[last] = idx;

    return indices;
}