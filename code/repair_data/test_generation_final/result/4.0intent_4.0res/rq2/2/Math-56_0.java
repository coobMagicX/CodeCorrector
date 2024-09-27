public int[] getCounts(int index) {
    if (index < 0 || index >= totalSize) {
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
        --idx;
        count -= offset;
        indices[i] = idx;
    }

    int idx = 0;  // Fixed from `int idx = 1` to `int idx = 0` to start counting from zero
    while (count <= index) {  // Corrected condition from `count < index` to `count <= index`
        count += uniCounterOffset[last];  // Use the correct offset for the last dimension
        ++idx;
    }
    --idx;
    indices[last] = idx;

    return indices;
}