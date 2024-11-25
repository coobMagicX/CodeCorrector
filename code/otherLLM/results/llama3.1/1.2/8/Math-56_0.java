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
        --idx;
        count -= offset;
        indices[i] = idx;
    }

    // Fix: Change the condition to 'count == index' to match the expected value
    int idx = 1;
    while (count < index) {
        count += idx;
        ++idx;
    }
    if (count > index) { // Check if the final count exceeds the target index
        --count; // Decrement count to match the target index
    } else if (count == index) { // If the count matches, assign the correct index value
        indices[last] = idx - 1;
    } else {
        throw new IllegalStateException("Count calculation failed"); // Throw an exception for unexpected count values
    }

    return indices;
}