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
        while (count + offset <= index) { // Change the condition to correctly calculate the indices
            count += offset;
            ++idx;
        }
        --idx; // Decrement idx after the loop ends for correct index calculation
        count -= offset; // Adjust the count back as we have overstepped
        indices[i] = idx;
    }

    int idx = 1;
    while (count < index) { // This condition is redundant since 'index' can be equal to totalSize, thus causing an infinite loop
        count += idx;
        ++idx;
    }
    --idx; // Decrement the last index as well
    indices[last] = idx;

    return indices;
}