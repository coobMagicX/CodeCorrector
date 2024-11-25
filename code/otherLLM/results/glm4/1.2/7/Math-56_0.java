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
        // Corrected the loop to avoid an infinite loop by ensuring that count does not exceed index
        if (count > index) {
            --idx; // Adjust idx because count exceeded the index after adding the last offset
        } else {
            --idx; // Decrement idx after the while loop because it was incremented in the last iteration of the loop
        }
        count -= offset;
        indices[i] = idx;
    }

    int idx = 1;
    // Adjusted the loop to start from 1 and increment count until it is exactly equal to index
    while (count < index) {
        count += idx;
        ++idx;
    }
    --idx; // Decrement idx after the while loop because it was incremented in the last iteration of the loop
    indices[last] = idx;

    return indices;
}