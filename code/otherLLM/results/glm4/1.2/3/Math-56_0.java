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
        // Corrected the logic to ensure that we subtract the offset after the loop
        if (count > index) {
            count -= offset; // Subtract one full offset since we've passed the 'index'
        } else {
            count = 0; // Reset count because it's still less than or equal to 'index'
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