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
        // Adjusted the while loop condition to use '<' instead of '<='
        while (count < index) {
            count += offset;
            ++idx;
        }
        --idx; // This line should be before assigning it to indices[i]
        count -= offset; // This line should be after determining idx, not before the assignment
        indices[i] = idx;
    }

    int idx = 1;
    while (count < index) {
        count += idx;
        ++idx;
    }
    --idx; // Decrement idx before assigning it to indices[last]
    indices[last] = idx;

    return indices;
}