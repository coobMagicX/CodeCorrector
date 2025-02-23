public int[] getCounts(int index) {
    if (index < 0 || index >= totalSize) {
        throw new OutOfRangeException(index, 0, totalSize);
    }

    final int[] indices = new int[dimension];

    int count = 0;
    for (int i = 0; i < last; i++) {
        int idx = 0;
        final int offset = uniCounterOffset[i];
        
        // Calculate how many complete units of 'offset' are there until 'index'
        while (count + offset <= index) {
            count += offset;
            idx++;
        }
        indices[i] = idx;
    }

    // Compute for the last dimension - we need to handle this separately
    // to ensure we get the precise final set of dimensions summing up to index.
    int idx = 0; // Start the index for the last dimension from 0
    while (count <= index) {
        ++idx;
        count++;
    }
    --idx; // Offset the last increment
    indices[last] = idx;

    return indices;
}
