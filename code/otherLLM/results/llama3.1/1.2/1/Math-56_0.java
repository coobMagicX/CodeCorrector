public int[] getCounts(int index) {
    if (index < 0 || 
        index >= totalSize) { // Check if index is within valid range
        throw new OutOfRangeException(index, 0, totalSize);
    }

    final int[] indices = new int[dimension];

    int count = 0;
    for (int i = 0; i <= last; i++) { // Iterate over all possible indices
        int idx = 0;
        final int offset = uniCounterOffset[i];
        while (count + offset <= index) { // Correctly calculate the multidimensional index
            count += offset;
            ++idx;
        }
        --idx;
        count -= offset;
        indices[i] = idx;
    }

    return indices; // Return the calculated multidimensional indices
}