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

    int idx = 0; // Changed initialization of idx from 1 to 0
    while (count < index) {
        count += uniCounterOffset[last]; // Changed from idx to use uniCounterOffset[last]
        ++idx;
    }
    --idx;
    indices[last] = idx;

    return indices;
}